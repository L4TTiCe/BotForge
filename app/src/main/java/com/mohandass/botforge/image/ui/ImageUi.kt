package com.mohandass.botforge.image.ui

import android.annotation.SuppressLint
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aallam.openai.api.BetaOpenAI
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.ChatType
import com.mohandass.botforge.chat.ui.components.chat.SendFloatingActionButton
import com.mohandass.botforge.chat.ui.components.header.HeaderWithActionIcon
import com.mohandass.botforge.chat.viewmodel.PersonaViewModel
import com.mohandass.botforge.common.Constants
import com.mohandass.botforge.common.Utils.Companion.formatDuration
import com.mohandass.botforge.common.services.snackbar.SwipeDirection
import com.mohandass.botforge.image.model.toInternal
import com.mohandass.botforge.image.ui.components.GeneratedImageHistoryItem
import com.mohandass.botforge.image.ui.components.NumberPicker
import com.mohandass.botforge.image.ui.components.dialogs.DeleteAllGeneratedImagesDialog
import com.mohandass.botforge.image.viewmodel.ImageViewModel
import com.slaviboy.composeunits.adh
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, BetaOpenAI::class, ExperimentalMaterialApi::class)
@Composable
fun ImageUi(
    imageViewModel: ImageViewModel = hiltViewModel(),
    appViewModel: AppViewModel = hiltViewModel(),
    personaViewModel: PersonaViewModel = hiltViewModel(),
) {
    var prompt by imageViewModel.prompt
    var imageSize by imageViewModel.imageSize
    val availableSizes = imageViewModel.availableSizes
    var n by imageViewModel.n

    val isLoading by imageViewModel.isLoading
    val showImage by imageViewModel.showImage
    val timeMillis by imageViewModel.timeMillis

    var size by remember { mutableStateOf(Size.Zero) }
    val swipeableState = rememberSwipeableState(SwipeDirection.Initial)
    val width = remember(size) {
        if (size.width == 0f) {
            1f
        } else {
            size.width
        }
    }

    if (swipeableState.isAnimationRunning) {
        DisposableEffect(Unit) {
            onDispose {
                when (swipeableState.currentValue) {
                    SwipeDirection.Right -> {
                        imageViewModel.previousImage()
                    }
                    SwipeDirection.Left -> {
                        imageViewModel.nextImage()
                    }

                    else -> {
                        return@onDispose
                    }
                }
            }
        }
    }

    val imageUri by imageViewModel.imageUri
    val currentImageIndex by imageViewModel.currentImageIndex
    val maxImageCount by imageViewModel.maxImageCount

    val openDeleteHistoryDialog by imageViewModel.openDeleteHistoryDialog

    val historyList = imageViewModel.history

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var showSizeList by remember {
        mutableStateOf(false)
    }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val userPreferences by appViewModel.appState.userPreferences.observeAsState()
    userPreferences?.let {
        if(it.enableImageGeneration.not()) {
            personaViewModel.clearSelection()
            personaViewModel.setChatType(ChatType.CREATE)
            appViewModel.appState.navControllerPersona.navigate(AppRoutes.MainRoutes.PersonaRoutes.Chat.route) {
                popUpTo(AppRoutes.MainRoutes.PersonaRoutes.Image.route) { inclusive = true }
            }
        }
    }

    if (openDeleteHistoryDialog) {
        DeleteAllGeneratedImagesDialog(
            onDismiss = {
                imageViewModel.updateDeleteDialogState(false)
            },
            onDelete = {
                imageViewModel.deleteAllGeneratedImages()
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            // Sends or Cancels request
            SendFloatingActionButton(
                isLoading = isLoading,
                onSend = {
                    imageViewModel.generateImageFromPrompt()
                    focusManager.clearFocus()
                },
                onCancel = {
                    imageViewModel.handleInterrupt()
                }
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 10.dp),
            state = listState,
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.picture),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.generate_images),
                        modifier = Modifier.padding(10.dp),
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    if (isLoading) {
                        Text(
                            text = formatDuration(timeMillis),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        // Fill max width
                        .fillMaxWidth()
                        // Set height to match width
                        .aspectRatio(1f)
                        .padding(horizontal = 10.dp)
                        .placeholder(
                            visible = showImage.not(),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                            highlight = PlaceholderHighlight.shimmer(
                                animationSpec = InfiniteRepeatableSpec(
                                    tween(
                                        durationMillis = 2000,
                                        delayMillis = 1000,
                                        easing = LinearEasing
                                    ),
                                    RepeatMode.Restart
                                )
                            ),
                            shape = MaterialTheme.shapes.medium
                        ),
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .swipeable(
                                state = swipeableState,
                                anchors = mapOf(
                                    -width to SwipeDirection.Left,
                                    0f to SwipeDirection.Initial,
                                    width to SwipeDirection.Right,
                                ),
                                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                                orientation = Orientation.Horizontal
                            ),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUri)
                            .placeholder(R.drawable.picture)
                            .crossfade(true)
                            .build(),
                        contentDescription = null
                    )
                }
            }

            item {
                if (showImage) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {

                        if (maxImageCount > 1 ) {
                            IconButton(
                                onClick = {
                                    imageViewModel.previousImage()
                                },
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = stringResource(id = R.string.previous_image_cd)
                                )
                            }

                            Text(
                                text = "${currentImageIndex + 1}/$maxImageCount",
                                modifier = Modifier
                                    .padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )

                            IconButton(
                                onClick = {
                                    imageViewModel.nextImage()
                                },
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = stringResource(id = R.string.next_image_cd)
                                )
                            }
                        }

                        IconButton(
                            modifier = Modifier
                                .padding(16.dp)
                                .padding(bottom = 10.dp)
                            ,
                            onClick = {
                                imageViewModel.shareImage(context)
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_share_24),
                                contentDescription = stringResource(id = R.string.share),
                            )
                        }
                    }
                }
            }

            item {
                Column {

                    Spacer(modifier = Modifier.height(0.02.adh))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedTextField(
                            value = prompt,
                            onValueChange = { prompt = it },
                            label = {
                                Text(text = stringResource(id = R.string.prompt))
                            },
                            trailingIcon = {
                                if (prompt.isNotBlank() || prompt.isNotEmpty()) {
                                    IconButton(onClick = {
                                        prompt = ""
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Clear,
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    imageViewModel.generateImageFromPrompt()
                                    focusManager.clearFocus()
                                }
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(id = R.string.image_size),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        Box {
                            OutlinedButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { showSizeList = !showSizeList }
                            ){
                                Text (imageSize.toInternal().toString())
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = null,
                                )
                            }
                            DropdownMenu(
                                expanded = showSizeList,
                                onDismissRequest = { showSizeList = false },
                            ) {
                                availableSizes.forEach { item ->
                                    DropdownMenuItem(
                                        onClick = {
                                            showSizeList = false
                                            imageSize = item.toImageSize()
                                        },
                                        text = { Text (item.toString()) }
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.number_of_images),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        NumberPicker(
                            modifier = Modifier
                                .weight(1f),
                            n = n,
                            onIncrement = {
                                if (n < Constants.MAX_IMAGE_GENERATION_COUNT) {
                                    n++
                                }
                            },
                            onDecrement = {
                                if (n > 1) {
                                    n--
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(0.01.adh))
                }
            }

            item {
                Divider()

                HeaderWithActionIcon(
                    text = stringResource(id = R.string.history),
                    leadingIcon = painterResource(id = R.drawable.baseline_history_24),
                    trailingIcon = painterResource(id = R.drawable.baseline_clear_all_24),
                    trailingIconOnClick = {
                        imageViewModel.updateDeleteDialogState(true)
                    }
                )
            }

            items(
                count = historyList.size,
                key = { index -> historyList[index].imageGenerationRequest.uuid }
            ) { idx ->
                GeneratedImageHistoryItem(
                    modifier = Modifier
                        .clickable {
                            imageViewModel.selectGeneratedImageGroup(historyList[idx].imageGenerationRequest.uuid)
                            // Scroll to top
                            coroutineScope.launch {
                                listState.animateScrollToItem(0)
                            }
                        },
                    imageGenerationRequestWithImages = historyList[idx],
                    onClickDelete = {
                        imageViewModel.deleteGeneratedImageGroup(
                            historyList[idx].imageGenerationRequest.uuid
                        )
                    },
                )
            }

            item {
                Spacer(modifier = Modifier.height(0.2.adh))
            }
        }
    }
}
