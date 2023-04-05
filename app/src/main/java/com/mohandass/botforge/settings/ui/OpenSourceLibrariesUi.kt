// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.ui.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R

@Composable
fun OpenSourceLibrariesUi(viewModel: AppViewModel) {
    LaunchedEffect(Unit) {
        viewModel.topBar.title.value = R.string.open_source_libraries
    }
    LibrariesContainer(
        modifier = Modifier
            .fillMaxSize(),
        showAuthor = true,
        showVersion = true,
        showLicenseBadges = true,
        colors = LibraryDefaults.libraryColors(
            backgroundColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            badgeBackgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
            badgeContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        ),
    )
}