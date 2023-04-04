package com.mohandass.botforge.chat.ui.components.chat.headers

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R

@Composable
fun PersonaChatHeader(
    modifier: Modifier = Modifier,
    personaName: String,
    expandCustomizePersona: Boolean,
    onExpandOrCollapse: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // Show Persona Name if available, else show "Chat"
        Text(
            text = if (personaName != "")
                stringResource(
                    id = R.string.chat_with_persona_name,
                    personaName
                )
            else
                stringResource(id = R.string.chat),
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .fillMaxWidth(0.85f),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.weight(1f))

        // Expand or Collapse Button
        IconButton(
            onClick = onExpandOrCollapse,
            modifier = Modifier.padding(10.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = if (expandCustomizePersona)
                        R.drawable.baseline_keyboard_arrow_up_24
                    else
                        R.drawable.baseline_keyboard_arrow_down_24
                ),
                contentDescription = stringResource(
                    id =
                    if (expandCustomizePersona)
                        R.string.show_less_cd
                    else
                        R.string.show_more_cd
                ),
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Preview
@Composable
fun PersonaChatHeaderPreview() {
    var isExpanded by remember { mutableStateOf(false) }

    PersonaChatHeader(
        personaName = "",
        expandCustomizePersona = isExpanded,
        onExpandOrCollapse = {
            isExpanded = !isExpanded
        }
    )
}

@Preview
@Composable
fun PersonaChatHeaderPreviewWithName() {
    var isExpanded by remember { mutableStateOf(false) }

    PersonaChatHeader(
        personaName = "John Doe",
        expandCustomizePersona = isExpanded,
        onExpandOrCollapse = {
            isExpanded = !isExpanded
        }
    )
}
