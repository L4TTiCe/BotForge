// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.image.ui

import android.annotation.SuppressLint
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.mohandass.botforge.chat.ui.components.ImageWithMessage
import com.mohandass.botforge.chat.ui.components.chat.SendFloatingActionButton
import com.mohandass.botforge.chat.ui.components.header.HeaderWithActionIcon
import com.mohandass.botforge.chat.viewmodel.PersonaViewModel
import com.mohandass.botforge.common.Constants
import com.mohandass.botforge.common.Utils.Companion.formatDuration
import com.mohandass.botforge.common.ui.components.DropdownButton
import com.mohandass.botforge.image.model.toInternal
import com.mohandass.botforge.image.ui.components.GeneratedImageHistoryItem
import com.mohandass.botforge.image.ui.components.NumberPicker
import com.mohandass.botforge.image.ui.components.dialogs.DeleteAllGeneratedImagesDialog
import com.mohandass.botforge.image.viewmodel.ImageViewModel
import com.slaviboy.composeunits.adh
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class, BetaOpenAI::class, ExperimentalFoundationApi::class
)
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

    val imageUriList = imageViewModel.imageUriList
    val maxImageCount by imageViewModel.maxImageCount

    val openDeleteHistoryDialog by imageViewModel.openDeleteHistoryDialog

    val historyList = imageViewModel.history

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        // provide pageCount
        maxImageCount
    }

    val userPreferences by appViewModel.appState.userPreferences.observeAsState()
    userPreferences?.let {
        if (it.enableImageGeneration.not()) {
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

                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = pagerState,
                        pageSpacing = 0.dp,
                        userScrollEnabled = true,
                        reverseLayout = false,
                        contentPadding = PaddingValues(0.dp),
                        beyondBoundsPageCount = 0,
                        pageSize = PageSize.Fill,
                        flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
                        key = null,
                        pageNestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
                            Orientation.Horizontal
                        ),
                        pageContent = { position ->
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageUriList[position])
                                    .placeholder(R.drawable.picture)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null
                            )
                        }
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

                        if (maxImageCount > 1) {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(
                                            pagerState.currentPage - 1
                                        )
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = stringResource(id = R.string.previous_image_cd)
                                )
                            }

                            Text(
                                text = "${pagerState.currentPage + 1}/$maxImageCount",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(
                                            pagerState.currentPage + 1
                                        )
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = stringResource(id = R.string.next_image_cd)
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                imageViewModel.generateImageVariant(
                                    pagerState.currentPage,
                                )
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_refresh_24),
                                contentDescription = stringResource(id = R.string.generate_variant),
                            )
                        }

                        IconButton(
                            onClick = {
                                imageViewModel.shareImage(context, pagerState.currentPage)
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

                        DropdownButton(
                            options = availableSizes,
                            selectedOption = imageSize.toInternal().toString(),
                            onOptionSelected = { imageSize = it.toImageSize() },
                        )
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
                            numberAsString = n.toString(),
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

            item {
                ImageWithMessage(
                    visible = historyList.isEmpty(),
                    painter = painterResource(id = R.drawable.empty_box),
                    imageContentDescription = null,
                    message = stringResource(id = R.string.no_images_yet),
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
