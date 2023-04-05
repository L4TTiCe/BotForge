// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mohandass.botforge.R

/**
 * A ViewModel to handle the top bar
 */
class TopBarViewModel : ViewModel() {
    private val _topBarTitle = mutableStateOf(R.string.app_name)
    val title: MutableState<Int>
        get() = _topBarTitle

    private val _overrideTopBarMenu = mutableStateOf(false)
    val overrideMenu: MutableState<Boolean>
        get() = _overrideTopBarMenu

    private val _topBarMenu = mutableStateOf<@Composable () -> Unit>({})
    val menu: MutableState<@Composable () -> Unit>
        get() = _topBarMenu
}
