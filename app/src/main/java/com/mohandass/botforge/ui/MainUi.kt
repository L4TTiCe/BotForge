package com.mohandass.botforge.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppState
import com.mohandass.botforge.ui.components.AvatarsBar
import com.mohandass.botforge.ui.components.chat.MessageList
import com.mohandass.botforge.ui.theme.BotForgeTheme
import com.mohandass.botforge.ui.viewmodels.AppViewModel
import com.slaviboy.composeunits.dh
import com.slaviboy.composeunits.dw
import com.mohandass.botforge.R.drawable as AppDrawable
import com.mohandass.botforge.R.string as AppText

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainUi(appState: AppState?, viewModel: AppViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopBar(appState = appState, viewModel = viewModel)
        },
        content = {
            Surface(
                modifier = Modifier.padding(it),
            ) {
                Column{
                    NavHost(
                        navController = navController,
                        startDestination = AppRoutes.MainRoutes.Default.route
                    ) {
                        composable(AppRoutes.MainRoutes.Default.route) {
                            CreateNewPersona(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CreateNewPersona(viewModel: AppViewModel) {

    val scrollState = rememberScrollState()

    val personaName by viewModel.personaName
    val personaSystemMessage by viewModel.personaSystemMessage

    BottomSheetScaffold(
        sheetContent = {
            Scaffold(modifier = Modifier
                .fillMaxSize(),
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        viewModel.autoAddMessage()
                    },
                    modifier = Modifier,
                ) {
                    Icon(
                        painter = painterResource(id = AppDrawable.plus),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                    Text (
                        text = "Send",
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            })
            {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {

                    Text(
                        text = if (personaName != "") "Chat with $personaName" else "Chat",
                        modifier = Modifier.padding(horizontal = 10.dp),
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(0.02.dh))

                    MessageList(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp)
                    )

                    Spacer(modifier = Modifier.height(0.15.dh))

                }

            }

        },
        sheetPeekHeight = 0.15.dh,
        sheetShape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetContentColor = MaterialTheme.colorScheme.onSurface,
        sheetElevation = 4.dp,
    ) {
        Column(modifier = Modifier
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
            .fillMaxSize()
        ) {
            Text(
                text = "Customize Persona",
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Create a persona to represent your bot",
                modifier = Modifier.padding(horizontal = 10.dp),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(0.02.dh))

            OutlinedTextField(
                value = personaName,
                onValueChange = { viewModel.updatePersonaName(it) },
                label = { Text(text = "Persona Name") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxSize()
            )

            Spacer(modifier = Modifier.height(0.02.dh))

            Text(
                text = "System Message",
                modifier = Modifier.padding(horizontal = 10.dp),
                style = MaterialTheme.typography.labelMedium,
            )

            OutlinedTextField(
                value = personaSystemMessage,
                onValueChange = { viewModel.updatePersonaSystemMessage(it) },
                placeholder = { Text(text = "You are a helpful assistant") },
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .sizeIn(minHeight = 0.3.dh, maxHeight = 0.5.dh)
                    .fillMaxSize()
            )

            Spacer(modifier = Modifier.height(0.02.dh))

            Row(horizontalArrangement = Arrangement.SpaceAround) {
                Button(
                    onClick = { /*TODO*/
                              viewModel.fetchPersonas()
                    },
                    modifier = Modifier.padding(horizontal = 10.dp)) {
                    Text(text = "Share")
                }

                Button(
                    onClick = { viewModel.savePersona() },
                ) {
                    Text(text = "Save")
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { /*TODO*/
                              Log.v("_personas", viewModel.personas.toString())
                    },
                    modifier = Modifier.padding(horizontal = 10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(0.01.dw))
                    Text(text = "Delete")
                }
            }



            Spacer(modifier = Modifier.height(0.2.dh))
        }
    }
}

@Composable
fun TopBar(appState: AppState?, viewModel: AppViewModel) {
    Surface(
        tonalElevation = 4.dp,
    ) {
        Column {
            Header(appState = appState, viewModel = viewModel)

            AvatarsBar(viewModel = viewModel)

            Spacer(modifier = Modifier.height(0.01.dh))
        }
    }
}

@Composable
fun Header(modifier: Modifier = Modifier, viewModel: AppViewModel, appState: AppState?) {
    var displayOptionsMenu by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = AppDrawable.logo),
            contentDescription = null,
            contentScale = ContentScale.Inside,
            modifier = modifier
                .size(0.25.dw)
                .padding(10.dp)
        )

        Text(
            // Apply H3 style
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(AppText.app_name),
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(10.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { displayOptionsMenu = true }) {
            Icon(
                painter = painterResource(id = AppDrawable.vert_more),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }

        Spacer(modifier = Modifier.width(0.02.dw))

        DropdownMenu(
            expanded = displayOptionsMenu,
            onDismissRequest = { displayOptionsMenu = false },
            offset =  DpOffset(0.8.dw, (-0.01).dh)
        ) {
            DropdownMenuItem(onClick = { viewModel.signOut {
                appState?.navigateTo(AppRoutes.Landing.route)
            } }, text = { Text(text = "Sign Out") })
        }

    }
}

@Preview(showBackground = true)
@Composable
fun MainUiPreview() {
    BotForgeTheme {
        MainUi(null)
    }
}
