package com.mohandass.botforge.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R
import com.mohandass.botforge.common.SnackbarManager
import com.mohandass.botforge.ui.components.buttons.TintedIconButton
import com.mohandass.botforge.viewmodels.AppViewModel
import com.mohandass.botforge.R.string as AppText

@Composable
fun AvatarsBar(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel,
) {
    val personas by viewModel.personas.observeAsState(listOf())

    LazyRow(modifier = modifier) {
        item {

            Spacer(modifier = Modifier.size(10.dp))

            Column {
                TintedIconButton(
                    icon = R.drawable.plus,
                    modifier = Modifier
                        .size(90.dp)
                        .padding(6.dp),
                    onClick = { viewModel.newPersona() }
                )

                if (viewModel.selectedPersona.value == "") {
                    ActiveIndicator()
                }
            }
            VerticalDivider()

        }
        personas?.let {
            items(it.size) { index ->

                if (viewModel.selectedPersona.value == personas!![index].uuid) {

                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        RoundedIconFromStringAnimated(
                            text = (
                                    if (personas!![index].alias != "")
                                        personas!![index].alias
                                    else
                                        personas!![index].name
                                    ),
                            modifier = Modifier.size(90.dp),
                            onClick = { viewModel.selectPersona(personas!![index].uuid) }
                        )

                        ActiveIndicator()
                    }

                } else {
                    RoundedIconFromString(
                        text = (
                                if (personas!![index].alias != "")
                                    personas!![index].alias
                                else
                                    personas!![index].name
                                ),
                        modifier = Modifier.size(90.dp),
                        onClick = { viewModel.selectPersona(personas!![index].uuid) }
                    )
                }
            }
        }

        if (personas?.size == 0) {
            item {
                var tint = MaterialTheme.colorScheme.onSurfaceVariant
                tint = tint.copy(alpha = 1f)

                TintedIconButton(
                    icon = R.drawable.logo,
                    modifier = Modifier
                        .size(90.dp)
                        .padding(6.dp),
                    onClick = {
                        SnackbarManager.showMessage(AppText.no_personas_yet)
                    },
                    iconTint = tint,
                    outerCircleColor = tint,
                    scale = 0.8f
                )
            }
        }
    }
}
