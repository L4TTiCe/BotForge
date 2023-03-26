package com.mohandass.botforge.sync.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.ui.components.icons.RoundedIconFromString
import com.mohandass.botforge.sync.model.dao.entities.BotE
import com.slaviboy.composeunits.dw

@Composable
fun BotCard(botE: BotE, viewModel: AppViewModel) {
    Card (
        modifier = Modifier
            .padding(5.dp)
            .width(
                if (0.9.dw > 400.dp)
                    400.dp
                else
                    0.9.dw
            )
            .clickable {},
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (
                modifier = Modifier.weight(1f)
            ){
                RoundedIconFromString(
                    text = (
                            if (botE.alias != "")
                                botE.alias
                            else
                                botE.name
                            ),
                    modifier = Modifier.size(90.dp),
                    borderColor = Color.Transparent,
                    onClick = { }
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(3f)
            ) {
                Text(
                    text = botE.name,
                    style = MaterialTheme.typography.labelLarge
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = botE.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {
                    viewModel.browse.makePersona(botE)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = "Add",
                        modifier = Modifier
                            .size(25.dp)
                    )
                }
            }

        }
    }
}