package com.mohandass.botforge.chat.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.image.ImageSize
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppState
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.services.OpenAiService
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import com.mohandass.botforge.common.services.snackbar.SnackbarMessage.Companion.toSnackbarMessageWithAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val openAiService: OpenAiService,
    private val appState: AppState,
    private val logger: Logger,
): ViewModel() {
    val prompt = mutableStateOf("")
    @OptIn(BetaOpenAI::class)
    val imageSize = mutableStateOf(ImageSize.is256x256)
    val n = mutableStateOf(1)

    val isLoading = mutableStateOf(false)
    val showImage = mutableStateOf(false)

    private fun setLoading(value: Boolean) {
        isLoading.value = value

        if (isLoading.value) {
            updateLastTimestamp()
            timerJob = viewModelScope.launch {
                while (true) {
                    delay(100)
                    _timeMillis.value = Date().time - _lastTimestamp.value
                }
            }
        } else {
            timerJob.cancel()
            _timeMillis.value = 0
        }
    }

    private val imageUriList = mutableStateListOf<String>()
    var currentImageIndex = mutableStateOf(0)
    var maxImageCount = mutableStateOf(imageUriList.size)

    val imageUri = mutableStateOf("")

    fun nextImage() {
        currentImageIndex.value = (currentImageIndex.value + 1) % imageUriList.size
        imageUri.value = imageUriList[currentImageIndex.value]
    }

    fun previousImage() {
        currentImageIndex.value = (currentImageIndex.value - 1) % imageUriList.size
        if (currentImageIndex.value < 0) currentImageIndex.value += imageUriList.size
        imageUri.value = imageUriList[currentImageIndex.value]
    }

    private lateinit var job: Job
    private lateinit var timerJob: Job

    private val _lastTimestamp = mutableStateOf(0L)
    private val _timeMillis = mutableStateOf(0L)
    val timeMillis: State<Long> = _timeMillis

    private fun updateLastTimestamp() {
        _lastTimestamp.value = Date().time
    }

    // Interrupts the current request, if any, to the OpenAI API
    private fun interruptRequest() {
        if (this::job.isInitialized) {
            job.cancel()
        }
        setLoading(false)
    }

    @OptIn(BetaOpenAI::class)
    fun generateImageFromPrompt() {
        if (prompt.value.isBlank()) {
            SnackbarManager.showMessage(R.string.prompt_cannot_be_empty)
            return
        }

        setLoading(true)

        job = viewModelScope.launch {
            try {
                val images = openAiService.generateImage(
                    prompt = prompt.value,
                    imageSize = imageSize.value,
                    n = n.value
                )

                imageUriList.clear()
                imageUriList.addAll(images.map { it.url })

                maxImageCount.value = imageUriList.size
                currentImageIndex.value = 0
                imageUri.value = imageUriList[currentImageIndex.value]

                showImage.value = true
            } catch (e: Exception) {
                logger.logError(TAG, "getChatCompletion() error: $e", e)
                if (e.message != null) {
                    logger.logError(TAG, "getChatCompletion() error m: ${e.message}", e)
                    SnackbarManager.showMessage(
                        e.toSnackbarMessageWithAction(R.string.settings) {
                            appState.navControllerMain.navigate(AppRoutes.MainRoutes.ApiKeySettings.route)
                        })
                } else {
                    logger.logError(TAG, "getChatCompletion() error st: ${e.stackTrace}", e)
                    val message = Utils.parseStackTraceForErrorMessage(e)

                    // Attempt to parse the error message
                    when (message.message) {
                        Utils.INVALID_API_KEY_ERROR_MESSAGE -> {
                            SnackbarManager.showMessageWithAction(
                                R.string.invalid_api_key,
                                R.string.settings
                            ) {
                                appState.navControllerMain.navigate(AppRoutes.MainRoutes.ApiKeySettings.route)
                            }
                        }

                        Utils.INTERRUPTED_ERROR_MESSAGE -> {
                            SnackbarManager.showMessage(R.string.request_cancelled)
                        }

                        else -> {
                            SnackbarManager.showMessage(
                                message.toSnackbarMessageWithAction(R.string.settings) {
                                    appState.navControllerMain.navigate(AppRoutes.MainRoutes.Settings.route)
                                })
                        }
                    }
                }
            }

            setLoading(false)
        }

    }

    companion object {
        private const val TAG = "ImageViewModel"
    }
}