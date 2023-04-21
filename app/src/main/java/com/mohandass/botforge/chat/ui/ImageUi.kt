package com.mohandass.botforge.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.mohandass.botforge.chat.viewmodel.ImageViewModel
import com.mohandass.botforge.common.Utils.Companion.formatDuration
import com.slaviboy.composeunits.adh

@OptIn(ExperimentalMaterial3Api::class, BetaOpenAI::class)
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .heightIn(min = 0.4.adh)
                .weight(1f)
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

            if (maxImageCount > 1) {

                IconButton(
                    onClick = {
                        imageViewModel.previousImage()
                    },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
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
                        .align(Alignment.BottomCenter)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                IconButton(
                    onClick = {
                        imageViewModel.nextImage()
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next"
                    )
                }
            }
        }

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = imageSize.equals(ImageSize.is256x256),
                    onClick = { imageSize = ImageSize.is256x256 }
                )
                Text(text = "256x256")
            }

            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = imageSize.equals(ImageSize.is512x512),
                    onClick = { imageSize = ImageSize.is512x512 }
                )
                Text(text = "512x512")
            }

            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = imageSize.equals(ImageSize.is1024x1024),
                    onClick = { imageSize = ImageSize.is1024x1024 }
                )
                Text(text = "1024x1024")
            }
        }

        Row (
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

        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            label = {
                Text(text = "Prompt")
            },
            trailingIcon = {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    IconButton(onClick = {
                        imageViewModel.generateImageFromPrompt()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Send,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(0.05.adh))
    }
}
