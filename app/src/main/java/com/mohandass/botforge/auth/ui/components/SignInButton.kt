package com.mohandass.botforge.auth.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R

@Composable
fun SignInButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FilledTonalButton(modifier = modifier, onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_google),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = "Sign In",
            modifier = Modifier.padding(8.dp)
        )
    }
}

