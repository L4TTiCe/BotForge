package com.mohandass.botforge.chat.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.R.string as AppText

@Composable
fun AvatarsBar(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel,
) {
//    val personas by viewModel.personas.observeAsState(listOf())
    val personas = viewModel.persona.personas
    val chatType by viewModel.chatType

    LazyRow(modifier = modifier) {
        item {

            Spacer(modifier = Modifier.size(10.dp))

            Column {
                TintedIconButton(
                    icon = R.drawable.plus,
                    iconTint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(90.dp)
                        .padding(6.dp),
                    isAnimated = chatType == AppViewModel.ChatType.CREATE,
                    onClick = { viewModel.showCreate() }
                )

                if (chatType == AppViewModel.ChatType.CREATE) {
                    ActiveIndicator()
                }
            }
        }

        item {
            Column {
                TintedIconButton(
                    icon = R.drawable.community,
                    iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                    scale = 0.8f,
                    modifier = Modifier
                        .size(90.dp)
                        .padding(6.dp),
                    isAnimated = chatType == AppViewModel.ChatType.BROWSE,
                    onClick = { viewModel.showBrowse() }
                )

                if (chatType == AppViewModel.ChatType.BROWSE) {
                    ActiveIndicator()
                }
            }
        }


        item {
            VerticalDivider()
        }

        items(personas.size) { index ->

            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {

                if (viewModel.persona.selectedPersona.value == personas[index].uuid) {
                    RoundedIconFromStringAnimated(
                        text = (
                                if (personas[index].alias != "")
                                    personas[index].alias
                                else
                                    personas[index].name
                                ),
                        modifier = Modifier.size(90.dp),
                        onClick = { viewModel.persona.selectPersona(personas[index].uuid) }
                    )

                    ActiveIndicator()

                } else {
                    RoundedIconFromString(
                        text = (
                                if (personas[index].alias != "")
                                    personas[index].alias
                                else
                                    personas[index].name
                                ),
                        modifier = Modifier.size(90.dp),
                        onClick = { viewModel.persona.selectPersona(personas[index].uuid) }
                    )

                    ActiveIndicator(modifier = Modifier.alpha(0f))

                }
            }

        }

        if (personas.size == 0) {
            item {
                var tint = MaterialTheme.colorScheme.onSurfaceVariant
                tint = tint.copy(alpha = 1f)

                TintedIconButton(
                    icon = R.drawable.logo,
                    modifier = Modifier
                        .size(90.dp)
                        .padding(6.dp),
                    onClick = {
                        SnackbarManager.showMessage(AppText.no_personas_yet)
                    },
                    iconTint = tint,
                    outerCircleColor = tint,
                    scale = 0.8f
                )
            }
        }

        item {

            Column {
                Column(
                    modifier = modifier.size(90.dp)
                        .clip(CircleShape)
                        .clickable { viewModel.showHistory() }.padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_bookmarks_24),
                        contentDescription = null,
                        modifier = modifier
                            .size(90.dp)
                            .aspectRatio(1f, matchHeightConstraintsFirst = true)
                            .padding(10.dp)
                            .clip(CircleShape)
                            .scale(0.8f)
                            .alpha(0.8f),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }

                if (chatType == AppViewModel.ChatType.HISTORY) {
                    ActiveIndicator()
                } else {
                    ActiveIndicator(modifier = Modifier.alpha(0f))
                }
            }

            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}
