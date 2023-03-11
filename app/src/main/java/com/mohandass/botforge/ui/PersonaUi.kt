package com.mohandass.botforge.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.ui.components.AvatarsBar
import com.mohandass.botforge.ui.components.chat.MessageList
import com.mohandass.botforge.viewmodels.AppViewModel
import com.slaviboy.composeunits.dh
import com.slaviboy.composeunits.dw

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PersonaUi(viewModel: AppViewModel) {

    val scrollState = rememberScrollState()
    val openDeleteDialog = remember { mutableStateOf(false) }

    val personaName by viewModel.personaName
    val personaSystemMessage by viewModel.personaSystemMessage

    if (openDeleteDialog.value) {
        AlertDialog(onDismissRequest = { openDeleteDialog.value = false },
            title = {
                Text(text = stringResource(id = R.string.delete_persona))
            },
            text = {
                Text(text = stringResource(id = R.string.delete_persona_message))
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deletePersona()
                    openDeleteDialog.value = false
                }) {
                    Text(text = stringResource(id = R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openDeleteDialog.value = false
                }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }

    Column {
        Surface(
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AvatarsBar(
                viewModel = viewModel
            )

            Spacer(
                modifier = Modifier.height(0.01.dh)
            )
        }

        BottomSheetScaffold(
            sheetContent = {
                Scaffold(modifier = Modifier
                    .fillMaxSize(),
                    floatingActionButton = {
                        Column(
                            horizontalAlignment = Alignment.End,
                        ) {
                            FloatingActionButton(
                                onClick = { viewModel.handleDelete(true) },
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = R.drawable.baseline_clear_all_24),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(15.dp))

                            ExtendedFloatingActionButton(
                                onClick = {
                                    TODO("Not yet implemented")
                                },
                                modifier = Modifier,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_send_24),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text (
                                    text = stringResource(id = R.string.send),
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                            }
                        }
                    })
                {
                    Surface(
                        tonalElevation = 0.1.dp,
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                        ) {

                            Text(
                                text = if (personaName != "")
                                    stringResource(
                                        id = R.string.chat_with_persona_name,
                                        personaName
                                    )
                                else
                                    stringResource(id = R.string.chat),
                                modifier = Modifier.padding(horizontal = 10.dp),
                                style = MaterialTheme.typography.headlineSmall
                            )

                            Spacer(modifier = Modifier.height(0.02.dh))

                            MessageList(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 10.dp),
                                viewModel = viewModel
                            )

                            Spacer(modifier = Modifier.height(0.15.dh))

                        }
                    }

                }

            },
            sheetPeekHeight = 0.15.dh,
            sheetShape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
            sheetElevation = 4.dp,
        ) {
            Column(modifier = Modifier
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
                .fillMaxSize()
            ) {

                Row {
                    Column {
                        Text(
                            text = stringResource(id = R.string.customise_persona),
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Text(
                            text = stringResource(id = R.string.create_persona_message),
                            modifier = Modifier.padding(horizontal = 10.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }


                }

                Spacer(modifier = Modifier.height(0.02.dh))

                OutlinedTextField(
                    value = personaName,
                    onValueChange = { viewModel.updatePersonaName(it) },
                    label = {
                        Text(text = stringResource(id = R.string.persona_name))
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxSize()
                )

                Spacer(modifier = Modifier.height(0.02.dh))

                Text(
                    text = stringResource(id = R.string.system_message),
                    modifier = Modifier.padding(horizontal = 10.dp),
                    style = MaterialTheme.typography.labelMedium,
                )

                OutlinedTextField(
                    value = personaSystemMessage,
                    onValueChange = { viewModel.updatePersonaSystemMessage(it) },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.system_message_hint)
                        )
                    },
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .sizeIn(minHeight = 0.2.dh, maxHeight = 0.4.dh)
                        .fillMaxSize()
                )

                Spacer(modifier = Modifier.height(0.02.dh))

                Row(horizontalArrangement = Arrangement.SpaceAround) {
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(horizontal = 10.dp)) {
                        Text(text = stringResource(id = R.string.share))
                    }

                    Button(
                        onClick = { viewModel.saveNewPersona() },
                    ) {
                        Text(text = stringResource(id = R.string.save))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    if (viewModel.selectedPersona.value != "") {
                        Button(
                            onClick = { openDeleteDialog.value = true },
                            modifier = Modifier.padding(horizontal = 10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                            Spacer(modifier = Modifier.width(0.01.dw))
                            Text(text = stringResource(id = R.string.delete))
                        }
                    }
                }

                if (viewModel.selectedPersona.value != "") {
                    Button(
                        onClick = { viewModel.saveAsNewPersona() },
                        modifier = Modifier.padding(horizontal = 10.dp)) {
                        Text(text = stringResource(id = R.string.make_copy))
                    }
                }

                Spacer(modifier = Modifier.height(0.2.dh))
            }
        }
    }
}

