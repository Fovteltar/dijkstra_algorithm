package ui.window

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.sp
import logger
import logic.SelectedTool
import logic.Tools

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun toolbar(
    modifier: Modifier,
    tools: Tools,
) {
    val selectedButton = remember { mutableStateOf(SelectedTool.NOTHING) }
    val columnColor = Color.Gray
    val columnProportion = mapOf(
        "file" to 0.20f,
        "lineSpacer" to 0.0025f,
        "voidSpacer" to 0.06f,
        "graph" to 0.10f,
        "start" to 0.20f,
        "arrow" to 0.20f,
    )
    // percent
    val buttonRoundedCorner = 25
    Column(
        modifier = Modifier
            .background(color = columnColor)
            .onKeyEvent {
                when {
                    (it.key == Key.Tab && it.type == KeyEventType.KeyDown) -> {
                        selectedButton.value = SelectedTool.NOTHING
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            .composed { modifier }
    ) {
        Button(
            onClick = {
//                TODO("NOT IMPLEMENTED YET")
            },
            modifier = Modifier
                .weight(columnProportion["file"]!!, true)
                .fillMaxWidth()
            ,
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = columnColor)
        ) {
            Text(
                text = "File",
                color = Color.Black,
                fontSize = 30.sp,
            )
        }
        Spacer(
            modifier = Modifier
                .weight(columnProportion["lineSpacer"]!!, true)
                .fillMaxWidth()
                .background(color = Color.Black)
        )
        Spacer(
            modifier = Modifier
                .weight(columnProportion["voidSpacer"]!!, true)
                .fillMaxWidth()
                .background(color = columnColor)
        )
        Button(
            onClick = {
                selectedButton.value = SelectedTool.ADD_VERTEX
            },
            modifier = Modifier
                .weight(columnProportion["graph"]!!, true)
                .fillMaxWidth()
            ,
            shape = RoundedCornerShape(buttonRoundedCorner),
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = if (selectedButton.value == SelectedTool.ADD_VERTEX) Color.Blue else Color.Black)
        ) {
            Text(
                text = "+V",
                color = Color.White,
                fontSize = 18.sp
            )
        }
        Spacer(
            modifier = Modifier
                .weight(columnProportion["voidSpacer"]!!, true)
                .fillMaxWidth()
                .background(color = columnColor)
        )
        Button(
            onClick = {
                selectedButton.value = SelectedTool.REMOVE_VERTEX
            },
            modifier = Modifier
                .weight(columnProportion["graph"]!!, true)
                .fillMaxWidth()
            ,
            shape = RoundedCornerShape(buttonRoundedCorner),
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = if (selectedButton.value == SelectedTool.REMOVE_VERTEX) Color.Red else Color.Black
            )
        ) {
            Text(
                text = "-V",
                color = Color.White,
                fontSize = 18.sp
            )
        }
        Spacer(
            modifier = Modifier
                .weight(columnProportion["voidSpacer"]!!, true)
                .fillMaxWidth()
                .background(color = columnColor)
        )
        Button(
            onClick = {
                selectedButton.value = SelectedTool.ADD_EDGE
            },
            modifier = Modifier
                .weight(columnProportion["graph"]!!, true)
                .fillMaxWidth()
            ,
            shape = RoundedCornerShape(buttonRoundedCorner),
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = if (selectedButton.value == SelectedTool.ADD_EDGE) Color.Blue else Color.Black
            )
        ) {
            Text(
                text = "+E",
                color = Color.White,
                fontSize = 18.sp,
            )
        }
        Spacer(
            modifier = Modifier
                .weight(columnProportion["voidSpacer"]!!, true)
                .fillMaxWidth()
                .background(color = columnColor)
        )
        Button(
            onClick = {
                selectedButton.value = SelectedTool.REMOVE_EDGE
            },
            modifier = Modifier
                .weight(columnProportion["graph"]!!, true)
                .fillMaxWidth()
            ,
            shape = RoundedCornerShape(buttonRoundedCorner),
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = if (selectedButton.value == SelectedTool.REMOVE_EDGE) Color.Red else Color.Black)
        ) {
            Text(
                text = "-E",
                color = Color.White,
                fontSize = 18.sp
            )
        }
        Spacer(
            modifier = Modifier
                .weight(columnProportion["voidSpacer"]!!, true)
                .fillMaxWidth()
                .background(color = columnColor)
        )
        Spacer(
            modifier = Modifier
                .weight(columnProportion["lineSpacer"]!!, true)
                .fillMaxWidth()
                .background(color = Color.Black)
        )
        Spacer(
            modifier = Modifier
                .weight(columnProportion["voidSpacer"]!!, true)
                .fillMaxWidth()
                .background(color = columnColor)
        )
        Button(
            onClick = {
//                TODO("NOT IMPLEMENTED YET")
            },
            modifier = Modifier
                .weight(columnProportion["start"]!!, true)
                .fillMaxWidth()
            ,
            shape = RoundedCornerShape(buttonRoundedCorner),
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = Color.Black
            )
        ) {
            Text(
                text = "START",
                color = Color.White,
                fontSize = 18.sp
            )
        }
        Spacer(
            modifier = Modifier
                .weight(columnProportion["voidSpacer"]!!, true)
                .fillMaxWidth()
                .background(color = columnColor)
        )
        Spacer(
            modifier = Modifier
                .weight(columnProportion["lineSpacer"]!!, true)
                .fillMaxWidth()
                .background(color = Color.Black)
        )
        Spacer(
            modifier = Modifier
                .weight(columnProportion["voidSpacer"]!!, true)
                .fillMaxWidth()
                .background(color = columnColor)
        )
        Row(
            modifier = Modifier
                .weight(columnProportion["arrow"]!!)
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
//                    TODO("NOT IMPLEMENTED YET")
                },
                modifier = Modifier
                    .weight(1f, true)
                    .fillMaxWidth()
                    .fillMaxHeight()
                ,
                shape = RoundedCornerShape(buttonRoundedCorner),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Black
                )
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.Red
                )
            }
            Button(
                onClick = {
//                    TODO("NOT IMPLEMENTED YET")
                },
                modifier = Modifier
                    .weight(1f, true)
                    .fillMaxWidth()
                    .fillMaxHeight()
                ,
                shape = RoundedCornerShape(buttonRoundedCorner),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Black
                )
            ) {
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }
    }
    tools.selectedTool = selectedButton.value
    logger.info(tools.selectedTool.name)
}