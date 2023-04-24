package com.mohandass.botforge.image.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohandass.botforge.R

@Composable
fun NumberPicker(
    modifier: Modifier = Modifier,
    n: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedButton(
            onClick = onDecrement,
            shape = MaterialTheme.shapes.extraLarge.copy(
                topEnd = CornerSize(0.dp),
                bottomEnd = CornerSize(0.dp)
            ),
        ) {
            Icon(
                painterResource(id = R.drawable.baseline_remove_24),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }

        OutlinedButton(
            modifier = Modifier
                .weight(1f)
                .offset((-1 * 1).dp, 0.dp),
            onClick = {},
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            )
        ) {
            Text(text = n.toString())
        }

        OutlinedButton(
            modifier = Modifier
                .offset((-1 * 2).dp, 0.dp),
            onClick = onIncrement,
            shape = MaterialTheme.shapes.extraLarge.copy(
                topStart = CornerSize(0.dp),
                bottomStart = CornerSize(0.dp)
            ),
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun NumberPickerPreview() {
    var n by remember {
        mutableStateOf(3)
    }

    NumberPicker(
        n = n,
        onIncrement = {
            n += 1
        },
        onDecrement = {
            n -= 1
        },
    )
}