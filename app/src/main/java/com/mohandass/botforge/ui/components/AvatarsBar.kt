package com.mohandass.botforge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.ui.viewmodels.AppViewModel

@Composable
fun AvatarsBar(modifier: Modifier = Modifier, viewModel: AppViewModel) {
    val personas by viewModel.personas.observeAsState(listOf())

    LazyRow(modifier = modifier) {
        item {
            AddAvatar(
                modifier = Modifier
                    .size(90.dp)
                    .padding(6.dp),
                onClick = { viewModel.newPersona() }
            )
            Box(
                modifier = Modifier
                    .height(90.dp) // Matches the height of other elements in the row
                    .width(18.dp),
                contentAlignment = Alignment.Center // Centers its content vertically and horizontally
            ) {
                Spacer(
                    modifier = Modifier
                        .height(64.dp) // Specifies the height of the spacer inside the box
                        .width(2.dp)
                        .background(color = MaterialTheme.colorScheme.onPrimaryContainer)
                )
            }
        }
        personas?.let {
            items(it.size) { index ->
    //            RoundedIcon(
    //                image = painterResource(id = R.drawable.logo),
    //                modifier = Modifier
    //                    .size(90.dp)
    //                    .padding(6.dp)
    //            )
                RoundedIconFromString(
                    text = personas!![index].name,
                    modifier = Modifier.size(90.dp),
                    onClick = { viewModel.selectPersona(personas!![index].uuid) }
                )
            }
        }
    }
}
