package com.mohandass.botforge.ui.components.buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SkipSignInButton(
    onClick: () -> Unit
) {
    TextButton(
        onClick = {
            onClick()
        },
        modifier = Modifier.padding(8.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Text(text = "Skip Login")
    }
}
