package com.mohandass.botforge.auth.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R

@Composable
fun SkipSignInButton(
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.padding(8.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Text(text = stringResource(id = R.string.anonymous_sign_in))
    }
}

@Preview
@Composable
fun SkipSignInButtonPreview() {
    SkipSignInButton(onClick = {})
}
