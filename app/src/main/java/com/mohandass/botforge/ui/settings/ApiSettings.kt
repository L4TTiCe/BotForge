package com.mohandass.botforge.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.resources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiSettings() {
    Text(
        text = "API Key",
        modifier = Modifier.padding(10.dp),
        style = MaterialTheme.typography.titleMedium
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
        ),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier.padding()
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    modifier = Modifier
                        .size(16.dp),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = resources().getString(R.string.caution),
                    style = MaterialTheme.typography.labelMedium,
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = resources().getString(R.string.api_key_warning_1),
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = resources().getString(R.string.api_key_warning_2),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }

    Spacer(modifier = Modifier.height(5.dp))

    Text(
        text = resources().getString(R.string.api_key),
        modifier = Modifier.padding(horizontal = 10.dp,),
        style = MaterialTheme.typography.labelMedium,
    )

    OutlinedTextField(
        value = "",
        onValueChange = { },
        placeholder = {
            Text(
                text = resources().getString(R.string.api_key_hint),
            )
        },
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 2.dp)
            .fillMaxWidth()
    )

    Text(
        text = resources().getString(
            R.string.api_key_info,
            resources().getString(R.string.api_key_link)),
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        style = MaterialTheme.typography.bodySmall,
    )

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            append("Tap here to visit ")
        }
        // attach a string annotation that stores a URL to the text "Android Developers".
        addStringAnnotation(
            tag = "URL",
            annotation = resources().getString(R.string.api_key_link),
            start = length,
            end = length + resources().getString(R.string.api_key_link).length
        )
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append("OpenAI API")
        }
    }

    val context = LocalContext.current

    ClickableText(
        text = annotatedString,
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        onClick = { offset ->
            // Get the URL string annotation attached at the offset position
            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    // Get the current context
                    // Launch an intent to open the URL
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(annotation.item)
                    }
                    context.startActivity(intent)
                }
        }
    )
}
