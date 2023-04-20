// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.mohandass.botforge.chat.viewmodel.TopBarViewModel
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.common.services.snackbar.SnackbarLauncherLocation
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import com.mohandass.botforge.settings.service.PreferencesDataStore

class AppState(
    preferencesDataStore: PreferencesDataStore,
    val logger: Logger
) {
    lateinit var resources: Resources

    // Keep the user preferences as a stream of changes
    private val userPreferencesFlow = preferencesDataStore.userPreferencesFlow
    val userPreferences = userPreferencesFlow.asLiveData()

    // Snackbar
    fun setActiveSnackbar(location: SnackbarLauncherLocation) {
        SnackbarManager.setSnackbarLocation(location)
        SnackbarManager.clearMessage()
    }

    // TopBar

    private val _topBarViewModel: TopBarViewModel = TopBarViewModel()
    val topBar: TopBarViewModel
        get() = _topBarViewModel


    // Navigation
    private var _navController: NavHostController? = null
    val navController: NavHostController
        get() = _navController!!

    // NacController in MainUI.kt
    private var _navControllerMain: NavController? = null
    val navControllerMain: NavController
        get() = _navControllerMain!!

    private var _navControllerPersona: NavController? = null
    val navControllerPersona: NavController
        get() = _navControllerPersona!!

    fun setNavController(navController: NavHostController) {
        _navController = navController
    }

    fun setNavControllerMain(navController: NavController) {
        _navControllerMain = navController
    }

    fun setNavControllerPersona(navController: NavController) {
        _navControllerPersona = navController
    }

    companion object {
        private const val TAG = "AppState"

        private var instance: AppState? = null

        fun getInstance(preferencesDataStore: PreferencesDataStore, logger: Logger): AppState {

            Log.v(TAG, "getInstance")

            if (instance == null) {
                instance = AppState(preferencesDataStore, logger)
            }
            return instance!!
        }
    }
}