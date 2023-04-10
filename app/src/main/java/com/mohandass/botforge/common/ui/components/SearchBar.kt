// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onClear: () -> Unit,
    label: String,
    onQueryChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        value = searchQuery,
        onValueChange = onQueryChange,
        label = {
            Text(label)
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_search_24),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
            )
        },
        trailingIcon = {
            IconButton(onClick = onClear) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_clear_24),
                    contentDescription = stringResource(id = R.string.clear),
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        )
    )
}
