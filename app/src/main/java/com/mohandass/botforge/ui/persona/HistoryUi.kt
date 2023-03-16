package com.mohandass.botforge.ui.persona

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.ui.components.RoundedIconFromString
import com.mohandass.botforge.viewmodels.AppViewModel
import com.mohandass.botforge.viewmodels.HistoryViewModel
import kotlinx.coroutines.delay
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

@Composable
fun HistoryUi(viewModel: AppViewModel, historyViewModel: HistoryViewModel) {
    val chats = historyViewModel.chats
    val personas = viewModel.personas

    LaunchedEffect(Unit) {
        viewModel.setLoading(true)
        historyViewModel.fetchChats(onSuccess = {
            viewModel.setLoading(false)
        })
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        tonalElevation = 0.1.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_bookmarks_24),
                    contentDescription = "Bookmarks",
                    modifier = Modifier
                )
                Text(
                    text = "Bookmarked",
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_clear_all_24),
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(chats.size) { index ->
                    val persona = remember {
                        mutableStateOf(personas.firstOrNull { it.uuid == chats[index].personaUuid })
                    }
                    val count = remember {
                        mutableStateOf(0)
                    }
                    val isDeleted = remember {
                        if (chats[index].personaUuid == "") {
                            mutableStateOf(false)
                        } else {
                            if (persona.value == null) {
                                mutableStateOf(true)
                            } else {
                                mutableStateOf(false)
                            }
                        }
                    }
                    val time = remember {
                        val timestamp = chats[index].savedAt
                        val date = Date(timestamp)
                        val prettyTime = PrettyTime()
                        prettyTime.locale = Locale.getDefault()
                        mutableStateOf(prettyTime.format(date))
                    }


                    LaunchedEffect(Unit) {
                        delay(200)
                        historyViewModel.getMessagesCount(chats[index].uuid) {
                            count.value = it
                        }
                    }

                    OutlinedCard (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSurface),
//                        elevation = CardDefaults.elevatedCardElevation()
                    ) {
                        Row {
                            Column {
                                if (persona.value != null) {
                                    RoundedIconFromString(
                                        text = (
                                                if (persona.value!!.alias != "")
                                                    persona.value!!.alias
                                                else
                                                    persona.value!!.name
                                                ),
                                        modifier = Modifier.size(90.dp),
                                        borderColor = Color.Transparent,
                                        onClick = {  }
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.logo),
                                        contentDescription = "Person",
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .size(80.dp),
                                        tint = Color.Unspecified
                                    )
                                }
                            }

                            Column (
                                modifier = Modifier.padding(10.dp),
                            ) {

                                Row {
                                    Column {
                                        Text(
                                            text = chats[index].name,
                                            style = MaterialTheme.typography.titleMedium
                                        )

                                        if (persona.value != null) {
                                            Text(
                                                text = (persona.value!!.name),
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        } else {
                                            if (isDeleted.value) {
                                                Text(
                                                    text = "Deleted Persona",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            } else {
                                                Text(
                                                    text = "Default Persona",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.weight(1f))

                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            modifier = Modifier
                                                .size(20.dp),
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Row (
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = count.value.toString(),
                                        style = MaterialTheme.typography.labelMedium
                                    )

                                    Spacer(modifier = Modifier.width(3.dp))

                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_message_24),
                                        contentDescription = "Message",
                                        modifier = Modifier
                                            .size(20.dp),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )

                                    Spacer(modifier = Modifier.weight(1f))

                                    Text(
                                        modifier = Modifier.padding(horizontal = 5.dp),
                                        text = time.value.toString(),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}