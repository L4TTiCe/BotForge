package com.mohandass.botforge.sync.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.ui.components.icons.RoundedIconFromString
import com.mohandass.botforge.chat.ui.components.icons.RoundedIconFromStringAnimated
import com.slaviboy.composeunits.dh

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharePersona(
    viewModel: AppViewModel
) {
    val personaName by viewModel.persona.personaName
    val personaAlias by viewModel.persona.personaAlias
    val personaSystemMessage by viewModel.persona.personaSystemMessage
    val personaDescription by viewModel.sharePersona.personaDescription
    val currentTag by viewModel.sharePersona.currentTag
    val personaTags = viewModel.sharePersona.personaTags


    BackHandler {
        viewModel.sharePersona.backHandler()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        tonalElevation = 0.1.dp,
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 10.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.community),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.community),
                        modifier = Modifier.padding(10.dp),
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = {
                        viewModel.sharePersona.backHandler()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                }
            }

            item {
                Row {
                    Column {
                        Text(
                            text = stringResource(id = R.string.share_persona),
                            modifier = Modifier.padding(horizontal = 10.dp),
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = stringResource(id = R.string.share_persona_message),
                            modifier = Modifier.padding(horizontal = 10.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(0.02.dh))
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(10.dp))

                    RoundedIconFromString(
                        text = (
                                if (personaAlias != "")
                                    personaAlias
                                else
                                    personaName
                                ),
                        modifier = Modifier.size(90.dp),
                        onClick = {}
                    )

                    Spacer(modifier = Modifier.weight(0.5f))

                    RoundedIconFromStringAnimated(
                        text = (
                               if (personaAlias != "")
                                   personaAlias
                               else
                                   personaName
                               ),
                        modifier = Modifier.size(90.dp),
                        onClick = {}
                    )

                    Spacer(modifier = Modifier.weight(0.5f))

                    OutlinedTextField(
                        value = personaAlias,
                        onValueChange = { viewModel.persona.updatePersonaAlias(it) },
                        label = {
                            Text(text = stringResource(id = R.string.alias))
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text),
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth(0.5f)
                    )
                }

                Spacer(modifier = Modifier.height(0.01.dh))

                Text(
                    text = stringResource(id = R.string.alias_message_1),
                    modifier = Modifier.padding(horizontal = 10.dp),
                    style = MaterialTheme.typography.labelLarge,
                )

                Spacer(modifier = Modifier.height(0.01.dh))

                Text(
                    text = stringResource(id = R.string.alias_message_2),
                    modifier = Modifier.padding(horizontal = 10.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )

                Spacer(modifier = Modifier.height(0.02.dh))
            }

            item {
                OutlinedTextField(
                    value = personaName,
                    onValueChange = { viewModel.persona.updatePersonaName(it) },
                    label = {
                        Text(text = stringResource(id = R.string.persona_name))
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(0.02.dh))
            }

            item {
                OutlinedTextField(
                    value = personaDescription,
                    onValueChange = { viewModel.sharePersona.updatePersonaDescription(it) },
                    label = {
                        Text(text = stringResource(id = R.string.description))
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.description_message_1),
                        )
                    },
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .sizeIn(minHeight = 0.1.dh)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(0.02.dh))
            }

            item {
                if (personaTags.size > 0) {
                    Row{
                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = stringResource(id = R.string.tags),
                            modifier = Modifier.padding(horizontal = 10.dp),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }

                LazyRow() {

                    item {
                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    items(personaTags.size) { idx ->
                        InputChip(
                            selected = true,
                            onClick = {},
                            label = { Text(text = personaTags[idx]) },
                            modifier = Modifier.padding(4.dp),
                            trailingIcon = {
                                IconButton(onClick = {
                                    viewModel.sharePersona.removeTag(personaTags[idx])
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                    )
                                }
                            }
                        )
                    }
                }

                OutlinedTextField(
                    value = currentTag,
                    onValueChange = { viewModel.sharePersona.updateCurrentTag(it) },
                    label = {
                        Text(text = stringResource(id = R.string.add_tags))
                    },
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(0.02.dh))
            }

            item {
                Text(
                    text = stringResource(id = R.string.system_message),
                    modifier = Modifier.padding(horizontal = 10.dp),
                    style = MaterialTheme.typography.labelLarge,
                )

                OutlinedTextField(
                    value = personaSystemMessage,
                    onValueChange = { viewModel.persona.updatePersonaSystemMessage(it) },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.system_message_hint)
                        )
                    },
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .sizeIn(minHeight = 0.2.dh)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(0.02.dh))
            }

            item {
                Row {
                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            viewModel.sharePersona.shareBot()
                        },
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_cloud_upload_24),
                                contentDescription = stringResource(id = R.string.share),
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(text = stringResource(id = R.string.share))
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(0.1.dh))
            }
        }

    }
}