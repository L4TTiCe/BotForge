package com.mohandass.botforge.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.resources
import com.mohandass.botforge.viewmodels.SettingsViewModel
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiSettings(settingsViewModel: SettingsViewModel) {
    val apiKey = remember { mutableStateOf(settingsViewModel.getApiKey()) }
    val showTokenInfoDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current

    if (showTokenInfoDialog.value) {
        AlertDialog(onDismissRequest = { showTokenInfoDialog.value = false },
            title = {
                Text(text = stringResource(id = R.string.managing_tokens))
            },
            text = {
                Column {
                    Text(text = stringResource(id = R.string.managing_tokens_message))

                    val annotatedStringManagingTokens = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                            append(resources().getString(R.string.learn_more_managing_tokens) + " ")
                        }
                        addStringAnnotation(
                            tag = "URL",
                            annotation = resources().getString(R.string.open_ai_managing_tokens_link),
                            start = length,
                            end = length + resources().getString(R.string.open_ai_managing_tokens_link).length
                        )
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append(resources().getString(R.string.open_ai_managing_tokens))
                        }
                    }

                    ClickableText(
                        text = annotatedStringManagingTokens,
                        modifier = Modifier.padding(vertical = 10.dp),
                        onClick = { offset ->
                            annotatedStringManagingTokens.getStringAnnotations(tag = "URL", start = offset, end = offset)
                                .firstOrNull()?.let { annotation ->
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse(annotation.item)
                                    }
                                    context.startActivity(intent)
                                }
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showTokenInfoDialog.value = false
                }) {
                    Text(text = stringResource(id = R.string.dismiss))
                }
            },
            dismissButton = {}
        )
    }

    Text(
        text = resources().getString(R.string.api_key),
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
        value = apiKey.value,
        onValueChange = {
            apiKey.value = it
        },
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
            append(resources().getString(R.string.tap_to_visit) + " ")
        }
        addStringAnnotation(
            tag = "URL",
            annotation = resources().getString(R.string.api_key_link),
            start = length,
            end = length + resources().getString(R.string.api_key_link).length
        )
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(resources().getString(R.string.open_ai_api_website))
        }
    }

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

    Spacer(modifier = Modifier.height(5.dp))

    OutlinedButton(
        modifier = Modifier.padding(horizontal = 10.dp),
        onClick = {
            settingsViewModel.setApiKey(apiKey.value)
        }
    ) {
        Text(text = resources().getString(R.string.save))
    }

    Spacer(modifier = Modifier.height(15.dp))

    Text(
        text = resources().getString(R.string.api_usage),
        modifier = Modifier.padding(horizontal = 10.dp,),
        style = MaterialTheme.typography.titleMedium,
    )

    Text(
        text = resources().getString(R.string.usage_quotas),
        modifier = Modifier.padding(10.dp),
        style = MaterialTheme.typography.labelMedium
    )

    Text(
        text = resources().getString(R.string.usage_quotas_message),
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        style = MaterialTheme.typography.bodySmall,
    )

    val annotatedStringPricing = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            append(resources().getString(R.string.learn_more_pricing) + " ")
        }
        addStringAnnotation(
            tag = "URL",
            annotation = resources().getString(R.string.open_ai_pricing_link),
            start = length,
            end = length + resources().getString(R.string.open_ai_pricing_link).length
        )
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(resources().getString(R.string.open_ai_pricing))
        }
    }

    ClickableText(
        text = annotatedStringPricing,
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        onClick = { offset ->
            annotatedStringPricing.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(annotation.item)
                    }
                    context.startActivity(intent)
                }
        }
    )

    Row (
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = resources().getString(R.string.your_usage),
            modifier = Modifier.padding(start = 10.dp),
            style = MaterialTheme.typography.labelMedium
        )

        IconButton(onClick = {
            showTokenInfoDialog.value = true
        }) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.baseline_token_24),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = settingsViewModel.getUsageTokens().toString(),
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.width(10.dp))
            }
            Row (
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.baseline_attach_money_24),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = DecimalFormat("0.0000")
                        .format(
                            resources().getFloat(R.dimen.gpt_3_5_turbo_cost_per_1k_tokens) *
                                    settingsViewModel.getUsageTokens().div(1000)
                        ).toString(),
                    modifier = Modifier.padding(end = 10.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }

    val annotatedStringUsage = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            append(resources().getString(R.string.visit) + " ")
        }
        addStringAnnotation(
            tag = "URL",
            annotation = resources().getString(R.string.open_ai_usage_link),
            start = length,
            end = length + resources().getString(R.string.open_ai_usage_link).length
        )
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(resources().getString(R.string.open_ai_usage))
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            append(" " + resources().getString(R.string.usage_summary))
        }
    }

    ClickableText(
        text = annotatedStringUsage,
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        onClick = { offset ->
            annotatedStringUsage.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(annotation.item)
                    }
                    context.startActivity(intent)
                }
        }
    )

    Row {
        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            modifier = Modifier.padding(horizontal = 10.dp),
            onClick = {
                settingsViewModel.resetUsageTokens()
            }
        ) {
            Text(
                text = resources().getString(R.string.reset_usage),
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(modifier = Modifier.width(4.dp))
    }
}
