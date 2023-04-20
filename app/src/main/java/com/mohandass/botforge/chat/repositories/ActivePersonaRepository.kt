// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.repositories

import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.mohandass.botforge.sync.service.BotService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/*
 * A Repository to hold the active persona
 *
 * This is used to pass the active persona between screens, and to
 * update the active persona
 */
class ActivePersonaRepository(
    private val botService: BotService
) {
    private val _activePersonaUuid = MutableStateFlow("")
    private val _activePersonaName = MutableStateFlow("")
    private val _activePersonaAlias = MutableStateFlow("")
    private val _activePersonaSystemMessage = MutableStateFlow("")
    private val _activePersonaParentUuid = MutableStateFlow("")
    private val _activePersonaParent = MutableStateFlow<BotE?>(null)

    val activePersonaUuid: StateFlow<String> = _activePersonaUuid
    val activePersonaName: StateFlow<String> = _activePersonaName
    val activePersonaAlias: StateFlow<String> = _activePersonaAlias
    val activePersonaSystemMessage: StateFlow<String> = _activePersonaSystemMessage
    val activePersonaParentUuid: StateFlow<String> = _activePersonaParentUuid
    val activePersonaParent: StateFlow<BotE?> = _activePersonaParent

    fun updateActivePersonaUuid(value: String) {
        _activePersonaUuid.value = value
    }
    fun updateActivePersonaName(value: String) {
        _activePersonaName.value = value
    }
    fun updateActivePersonaAlias(value: String) {
        _activePersonaAlias.value = value
    }
    fun updateActivePersonaSystemMessage(value: String) {
        _activePersonaSystemMessage.value = value
    }
    fun updateActivePersonaParentUuid(uuid: String) {
        _activePersonaParentUuid.value = uuid

        if (uuid.isNotEmpty() || uuid.isNotBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                updateParentBot(botService.getBot(uuid))
            }
        } else {
            updateParentBot(null)
        }
    }
    private fun updateParentBot(bot: BotE?) {
        _activePersonaParent.value = bot
    }

    fun clear() {
        _activePersonaUuid.value = ""
        _activePersonaName.value = ""
        _activePersonaAlias.value = ""
        _activePersonaSystemMessage.value = ""
        _activePersonaParentUuid.value = ""
        _activePersonaParent.value = null
    }
}