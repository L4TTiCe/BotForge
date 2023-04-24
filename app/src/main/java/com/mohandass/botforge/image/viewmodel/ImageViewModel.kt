package com.mohandass.botforge.image.viewmodel

import android.graphics.BitmapFactory
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
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.common.services.OpenAiService
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import com.mohandass.botforge.common.services.snackbar.SnackbarMessage.Companion.toSnackbarMessageWithAction
import com.mohandass.botforge.image.model.ImageSizeInternal
import com.mohandass.botforge.image.model.dao.entities.GeneratedImageE
import com.mohandass.botforge.image.model.dao.entities.ImageGenerationRequestE
import com.mohandass.botforge.image.model.dao.entities.relations.ImageGenerationRequestWithImages
import com.mohandass.botforge.image.model.toInternal
import com.mohandass.botforge.image.services.implementations.ImageGenerationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URL
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val openAiService: OpenAiService,
    private val imageGenerationService: ImageGenerationService,
    private val appState: AppState,
    private val logger: Logger,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
): ViewModel() {
    val prompt = mutableStateOf("")
    @OptIn(BetaOpenAI::class)
    val imageSize = mutableStateOf(ImageSize.is256x256)
    val n = mutableStateOf(1)

    val availableSizes = listOf(
        ImageSizeInternal.is256x256,
        ImageSizeInternal.is512x512,
        ImageSizeInternal.is1024x1024,
    )

    val isLoading = appState.isImageLoading
    val showImage = mutableStateOf(false)

    val history = mutableStateListOf<ImageGenerationRequestWithImages>()
    lateinit var deleteJob: Job

    private val _openDeleteHistoryDialog = mutableStateOf(false)
    val openDeleteHistoryDialog: State<Boolean> = _openDeleteHistoryDialog

    fun updateDeleteDialogState(state: Boolean) {
        _openDeleteHistoryDialog.value = state
    }

    init {
        fetchHistory()
    }

    private fun fetchHistory(
        onSuccess: () -> Unit = {}
    ) {
        logger.logVerbose(TAG, "fetchHistory()")
        viewModelScope.launch {
            history.clear()
            history.addAll(imageGenerationService.getPastImageGenerations())
            onSuccess()
        }
    }

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

    private val imageUriList = mutableStateListOf<Any>()
    var currentImageIndex = mutableStateOf(0)
    var maxImageCount = mutableStateOf(imageUriList.size)

    val imageUri = mutableStateOf<Any>("")

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

    fun handleInterrupt() {
        SnackbarManager.showMessageWithAction(R.string.waiting_for_response, R.string.cancel) {
            interruptRequest()
        }
    }

    // Interrupts the current request, if any, to the OpenAI API
    private fun interruptRequest() {
        if (this::job.isInitialized) {
            job.cancel()
        }
        setLoading(false)
    }

    @OptIn(BetaOpenAI::class)
    private fun saveGeneratedImages(
        onSuccess: () -> Unit = {},
    ) {
        val imageGenerationRequestE = ImageGenerationRequestE(
            prompt = prompt.value,
            n = n.value,
            imageSize = imageSize.value.toInternal(),
        )

        viewModelScope.launch(ioDispatcher) {
            val generatedImages = imageUriList.map {
                // Create a URL object from the URL string
                val url = URL(it as String? ?: "")

                // Open a connection to the URL and get an input stream
                val inputStream = url.openStream()

                // Decode the input stream into a bitmap
                val bitmap = BitmapFactory.decodeStream(inputStream)

                // Close the input stream
                inputStream.close()
                GeneratedImageE(
                    bitmap = bitmap,
                    generationRequestUuid = imageGenerationRequestE.uuid,
                )
            }

            logger.logVerbose(
                TAG,
                "saveGeneratedImages() imageGenerationRequestE: $imageGenerationRequestE"
            )
            logger.logVerbose(TAG, "saveGeneratedImages() generatedImages: $generatedImages")

            imageGenerationService.saveImageGenerationRequestAndImages(
                request = imageGenerationRequestE,
                images = generatedImages
            ) {
                fetchHistory() {
                    selectGeneratedImageGroup(imageGenerationRequestE.uuid)
                    onSuccess()
                }
            }
        }
    }

    @OptIn(BetaOpenAI::class)
    fun selectGeneratedImageGroup(requestId: String) {
        logger.logVerbose(TAG, "selectGeneratedImageGroup() requestId: $requestId")
        val imageGenerationRequestWithImages = history.firstOrNull {
            it.imageGenerationRequest.uuid == requestId
        } ?: return

        val images = imageGenerationRequestWithImages.generatedImages.map { it.bitmap }

        imageUriList.clear()

        images.forEach {
            imageUriList.add(it!!)
        }

        logger.logVerbose(TAG, "selectGeneratedImageGroup() imageUriList: $imageUriList")

        prompt.value = imageGenerationRequestWithImages.imageGenerationRequest.prompt
        imageSize.value = imageGenerationRequestWithImages.imageGenerationRequest.imageSize!!.toImageSize()

        maxImageCount.value = imageUriList.size
        currentImageIndex.value = 0
        imageUri.value = imageUriList[currentImageIndex.value]
        showImage.value = true
    }

    fun deleteGeneratedImageGroup(requestId: String) {
        deleteJob = viewModelScope.launch(ioDispatcher) {
            logger.logVerbose(TAG, "deleteGeneratedImage() requestId: $requestId")
            history.removeIf { it.imageGenerationRequest.uuid == requestId }

            delay(5000)
            imageGenerationService.deleteImageGenerationRequestById(
                requestId
            )
        }

        SnackbarManager.showMessageWithAction(
            R.string.image_group_deleted,
            R.string.undo
        ) {
            deleteJob.cancel()
            fetchHistory()
        }
    }

    fun deleteAllGeneratedImages() {
        logger.logVerbose(TAG, "deleteAllGeneratedImages()")
        viewModelScope.launch {
            imageGenerationService.deleteAllImageGenerationRequests()
            fetchHistory()
        }
    }

    @OptIn(BetaOpenAI::class)
    fun generateImageFromPrompt() {
        if (prompt.value.isBlank()) {
            SnackbarManager.showMessage(R.string.prompt_cannot_be_empty)
            return
        }

        showImage.value = false
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

                saveGeneratedImages() {
                    setLoading(false)
                }

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

                    setLoading(false)

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
        }

    }

    companion object {
        private const val TAG = "ImageViewModel"
    }
}
