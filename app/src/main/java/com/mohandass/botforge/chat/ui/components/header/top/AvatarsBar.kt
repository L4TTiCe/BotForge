// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.ui.components.header.top

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.ChatType
import com.mohandass.botforge.chat.ui.components.header.ActiveIndicator
import com.mohandass.botforge.chat.ui.components.header.VerticalDivider
import com.mohandass.botforge.chat.ui.components.icons.AvatarBarIcon
import com.mohandass.botforge.chat.ui.components.icons.RoundedIconFromString
import com.mohandass.botforge.chat.ui.components.icons.RoundedIconFromStringAnimated
import com.mohandass.botforge.chat.ui.components.icons.TintedIconButton
import com.mohandass.botforge.chat.viewmodel.PersonaViewModel
import com.mohandass.botforge.common.Constants
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import com.mohandass.botforge.R.string as AppText

@Composable
fun AvatarsBar(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel = hiltViewModel(),
    personaViewModel: PersonaViewModel = hiltViewModel(),
) {
    val personas by personaViewModel.personas.observeAsState(initial = emptyList())
    val chatType by personaViewModel.chatType

    val activePersona by personaViewModel.personaUuid.collectAsState()

    var isUserGeneratedContentEnabled by remember {
        mutableStateOf(false)
    }
    var isImageGenerationEnabled by remember {
        mutableStateOf(true)
    }

    val userPreferences by appViewModel.appState.userPreferences.observeAsState()
    userPreferences?.let {
        isUserGeneratedContentEnabled = it.enableUserGeneratedContent
        isImageGenerationEnabled = it.enableImageGeneration
    }

    LazyRow(modifier = modifier) {
        item {

            Spacer(modifier = Modifier.size(10.dp))

            Column {
                TintedIconButton(
                    icon = R.drawable.plus,
                    iconTint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(Constants.ICONS_SIZE.dp)
                        .padding(6.dp),
                    isAnimated = chatType == ChatType.CREATE,
                    contentDescription = stringResource(id = AppText.create_persona),
                    onClick = { personaViewModel.showCreate() }
                )

                if (chatType == ChatType.CREATE) {
                    ActiveIndicator()
                }
            }
        }

        if (isImageGenerationEnabled) {
            item {
                Column {
                    TintedIconButton(
                        icon = R.drawable.picture,
                        iconTint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(Constants.ICONS_SIZE.dp)
                            .padding(6.dp),
                        isAnimated = chatType == ChatType.IMAGE,
                        contentDescription = null,
                        onClick = {
                            personaViewModel.showImage()
                        }
                    )

                    if (chatType == ChatType.IMAGE) {
                        ActiveIndicator()
                    }
                }
            }
        }

        if (isUserGeneratedContentEnabled) {
            item {
                Column {
                    TintedIconButton(
                        icon = R.drawable.community,
                        iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                        scale = 0.8f,
                        modifier = Modifier
                            .size(Constants.ICONS_SIZE.dp)
                            .padding(6.dp),
                        isAnimated = chatType == ChatType.BROWSE || chatType == ChatType.SHARE,
                        contentDescription = stringResource(id = AppText.community_ab_icon_cd),
                        onClick = { personaViewModel.showMarketplace() }
                    )

                    if (chatType == ChatType.BROWSE || chatType == ChatType.SHARE) {
                        ActiveIndicator()
                    }
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

                if (activePersona == personas[index].uuid) {
                    RoundedIconFromStringAnimated(
                        text = (
                                if (personas[index].alias != "")
                                    personas[index].alias
                                else
                                    personas[index].name
                                ),
                        modifier = Modifier.size(Constants.ICONS_SIZE.dp),
                        onClick = { personaViewModel.selectPersona(personas[index].uuid) }
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
                        modifier = Modifier.size(Constants.ICONS_SIZE.dp),
                        onClick = { personaViewModel.selectPersona(personas[index].uuid) }
                    )

                    ActiveIndicator(modifier = Modifier.alpha(0f))

                }
            }

        }

        if (personas.isEmpty()) {
            item {
                var tint = MaterialTheme.colorScheme.onSurfaceVariant
                tint = tint.copy(alpha = 1f)

                TintedIconButton(
                    icon = R.drawable.logo,
                    contentDescription = stringResource(id = AppText.persona_placeholder_cd),
                    modifier = Modifier
                        .size(Constants.ICONS_SIZE.dp)
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
                AvatarBarIcon(
                    icon = painterResource(id = R.drawable.baseline_bookmarks_24),
                    iconContentDescription = stringResource(id = AppText.show_bookmarks_cd)
                ) {
                    personaViewModel.showHistory()
                }

                if (chatType == ChatType.HISTORY) {
                    ActiveIndicator()
                } else {
                    ActiveIndicator(modifier = Modifier.alpha(0f))
                }
            }
        }

        item {
            Column {
                AvatarBarIcon(
                    icon = painterResource(id = R.drawable.list),
                    iconContentDescription = "List View"
                ) {
                    personaViewModel.showList()
                }

                if (chatType == ChatType.LIST) {
                    ActiveIndicator()
                } else {
                    ActiveIndicator(modifier = Modifier.alpha(0f))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.width(15.dp))
        }
    }
}
