package com.mohandass.botforge.auth.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R

@Composable
fun SignInButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FilledTonalButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(
            text = stringResource(id = R.string.sign_in_or_sign_up),
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview
@Composable
fun SignInButtonPreview() {
    SignInButton(onClick = {})
}

