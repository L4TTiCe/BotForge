package com.mohandass.botforge.image.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.image.ImageSize
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.mohandass.botforge.R
import com.mohandass.botforge.common.Utils.Companion.formatDuration
import com.mohandass.botforge.image.ui.components.GeneratedImageHistoryItem
import com.mohandass.botforge.image.viewmodel.ImageViewModel
import com.slaviboy.composeunits.adh

@OptIn(ExperimentalMaterial3Api::class, BetaOpenAI::class, ExperimentalMaterialApi::class)
@Composable
fun ImageUi(
    imageViewModel: ImageViewModel = hiltViewModel(),
) {
    var prompt by imageViewModel.prompt
    var imageSize by imageViewModel.imageSize
    var n by imageViewModel.n

    val isLoading by imageViewModel.isLoading
    val showImage by imageViewModel.showImage
    val timeMillis by imageViewModel.timeMillis

    val imageUri by imageViewModel.imageUri
    val currentImageIndex by imageViewModel.currentImageIndex
    val maxImageCount by imageViewModel.maxImageCount

    val historyList = imageViewModel.history

    val focusManager = LocalFocusManager.current

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize(),
        sheetPeekHeight = 0.15.adh,
        sheetShape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
        sheetElevation = 4.dp,
        sheetContent = {
            Surface(
                tonalElevation = 4.dp,
            ) {
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
                                Text(text = "Prompt")
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

                        if (isLoading) {
                            IconButton(onClick = {
                                imageViewModel.handleInterrupt()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(24.dp)
                                )
                            }
                        } else {
                            IconButton(onClick = {
                                imageViewModel.generateImageFromPrompt()
                                focusManager.clearFocus()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Send,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(20.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = imageSize.equals(ImageSize.is256x256),
                                onClick = { imageSize = ImageSize.is256x256 }
                            )
                            Text(text = "256x256")
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = imageSize.equals(ImageSize.is512x512),
                                onClick = { imageSize = ImageSize.is512x512 }
                            )
                            Text(text = "512x512")
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = imageSize.equals(ImageSize.is1024x1024),
                                onClick = { imageSize = ImageSize.is1024x1024 }
                            )
                            Text(text = "1024x1024")
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        IconButton(onClick = {
                            if (n > 1) {
                                n--
                            }
                        }) {
                            Icon(
                                painterResource(id = R.drawable.baseline_remove_24),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Text(
                            text = "$n",
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        IconButton(onClick = {
                            if (n < 5) {
                                n++
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(0.05.adh))
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 10.dp),
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
                        text = "Image Generation",
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
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = MaterialTheme.shapes.medium
                        ),
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth(),
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
                if (maxImageCount > 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {

                        IconButton(
                            onClick = {
                                imageViewModel.previousImage()
                            },
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Previous"
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
                                contentDescription = "Next"
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_history_24),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "History",
                        modifier = Modifier.padding(10.dp),
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.weight(1f))

                }

                Divider()
            }

            items(
                count = historyList.size,
                key = { index -> historyList[index].imageGenerationRequest.uuid }
            ) { idx ->
                GeneratedImageHistoryItem(
                    imageGenerationRequestWithImages = historyList[idx],
                    onClickDelete = {
                        // TODO
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(0.2.adh))
            }
        }
    }
}
