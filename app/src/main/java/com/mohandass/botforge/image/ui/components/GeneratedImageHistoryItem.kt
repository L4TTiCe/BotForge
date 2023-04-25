// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.image.ui.components

import android.graphics.Bitmap
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mohandass.botforge.R
import com.mohandass.botforge.image.model.ImageSizeInternal
import com.mohandass.botforge.image.model.dao.entities.GeneratedImageE
import com.mohandass.botforge.image.model.dao.entities.ImageGenerationRequestE
import com.mohandass.botforge.image.model.dao.entities.relations.ImageGenerationRequestWithImages
import org.ocpsoft.prettytime.PrettyTime
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GeneratedImageHistoryItem(
    modifier: Modifier = Modifier,
    imageGenerationRequestWithImages: ImageGenerationRequestWithImages,
    onClickDelete: () -> Unit,
) {

    var isExpanded by remember {
        mutableStateOf(false)
    }
    val textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    val time by remember {
        val timestamp = imageGenerationRequestWithImages.imageGenerationRequest.timestamp
        val date = Date(timestamp)
        val prettyTime = PrettyTime()
        prettyTime.locale = Locale.getDefault()
        mutableStateOf(prettyTime.format(date))
    }

    Column(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(modifier = Modifier.weight(0.8f)) {
                Text(
                    modifier = Modifier
                        .animateContentSize()
                        .clickable {
                            isExpanded = !isExpanded
                        },
                    text = imageGenerationRequestWithImages.imageGenerationRequest.prompt,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = {
                        if (textLayoutResult.value == null)
                            textLayoutResult.value = it
                    },
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            IconButton(onClick = onClickDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.delete),
                    modifier = Modifier
                        .size(20.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        FlowRow {
            for (generatedImage in imageGenerationRequestWithImages.generatedImages) {
                AsyncImage(
                    modifier = Modifier,
                    model = generatedImage.bitmap,
                    contentDescription = imageGenerationRequestWithImages.imageGenerationRequest.prompt,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = time.toString(),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview
@Composable
fun GeneratedImageHistoryItemPreview() {
    val request = ImageGenerationRequestE(
        prompt = "A sample prompt",
        n = 2,
        imageSize = ImageSizeInternal.is256x256
    )

    val generatedImage1 = GeneratedImageE(
        generationRequestUuid = request.uuid,
        bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888)
    )
    val generatedImage2 = GeneratedImageE(
        generationRequestUuid = request.uuid,
        bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888)
    )

    val data = ImageGenerationRequestWithImages(
        imageGenerationRequest = request,
        generatedImages = listOf(generatedImage1, generatedImage2)
    )

    GeneratedImageHistoryItem(
        imageGenerationRequestWithImages = data,
        onClickDelete = { }
    )
}
