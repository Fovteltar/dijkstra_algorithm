package application.ui.window

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.sp
import application.controller.SelectedTool
import application.controller.Tools
import logger

class Toolbar(val tools: Tools) {
    val selectedButtonState = mutableStateOf(SelectedTool.NOTHING)

    @Composable
    fun draw(modifier: Modifier) {
        val isAlgoStarted = remember { tools.isAlgoStarted }
        val selectedButton = remember { selectedButtonState }
        val columnColor = Color.Gray
        val columnProportion = mapOf(
            "file" to 1f,
            "lineSpacer" to 0.0125f,
            "voidSpacer" to 0.3f,
            "graph" to 0.5f,
            "start" to 1f,
            "algoButtons" to 1f,
            "arrow" to 1f,
        )

        if (isAlgoStarted.value) {
            selectedButton.value = SelectedTool.NOTHING
        }

        // percent
        val buttonRoundedCorner = 25
        Column(
            modifier = Modifier
                .background(color = columnColor)
                .onKeyEvent {
                    tools.notifyMe(Pair(this, it))
                    true
                }
                .composed { modifier }
        ) {
            Row(modifier = Modifier.weight(columnProportion["file"]!!, true)) {
                Button(
                    onClick = {
                        if (isAlgoStarted.value) {
                            isAlgoStarted.value = false
                        }
                        tools.notifyMe(Pair(getSuper(), "load"))
                    },
                    modifier = Modifier
                        .weight(1f, true)
                        .fillMaxHeight(),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = columnColor)
                ) {
                    Text(
                        text = "Load",
                        color = Color.Black,
                        fontSize = 14.sp,
                    )
                }
                Spacer(
                    modifier = Modifier
                        .weight(columnProportion["lineSpacer"]!!, true)
                        .fillMaxWidth()
                        .background(color = Color.Black)
                )
                Button(
                    onClick = {
                        tools.notifyMe(Pair(getSuper(), "save"))
                    },
                    modifier = Modifier
                        .weight(1f, true)
                        .fillMaxHeight(),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = columnColor)
                ) {
                    Text(
                        text = "Save",
                        color = Color.Black,
                        fontSize = 14.sp,
                    )
                }
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
                    if (!isAlgoStarted.value) {
                        selectedButton.value = SelectedTool.ADD_VERTEX
                    }
                },
                modifier = Modifier
                    .weight(columnProportion["graph"]!!, true)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(buttonRoundedCorner),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = if (selectedButton.value == SelectedTool.ADD_VERTEX) Color.Blue else Color.Black
                )
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
                    if (!isAlgoStarted.value) {
                        selectedButton.value = SelectedTool.REMOVE_VERTEX
                    }
                },
                modifier = Modifier
                    .weight(columnProportion["graph"]!!, true)
                    .fillMaxWidth(),
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
                    if (!isAlgoStarted.value) {
                        selectedButton.value = SelectedTool.ADD_EDGE
                    }
                },
                modifier = Modifier
                    .weight(columnProportion["graph"]!!, true)
                    .fillMaxWidth(),
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
                    if (!isAlgoStarted.value) {
                        selectedButton.value = SelectedTool.REMOVE_EDGE
                    }
                },
                modifier = Modifier
                    .weight(columnProportion["graph"]!!, true)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(buttonRoundedCorner),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = if (selectedButton.value == SelectedTool.REMOVE_EDGE) Color.Red else Color.Black
                )
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
                    tools.notifyMe(Pair(getSuper(), "changeAlgoState"))
                },
                modifier = Modifier
                    .weight(columnProportion["start"]!!, true)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(buttonRoundedCorner),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = if (isAlgoStarted.value) Color.Yellow else Color.Black
                )
            ) {
                Text(
                    text = if (isAlgoStarted.value) "END" else "START",
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
                    .weight(columnProportion["algoButtons"]!!)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (tools.isAlgoStarted.value) {
                            tools.notifyMe(Pair(getSuper(), "play"))
                        }
                    },
                    modifier = Modifier
                        .weight(1f, true)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(buttonRoundedCorner),
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = Color.Black
                    )
                ) {
                    Icon(
                        if (tools.isPlayingNow.value) Icons.Default.Close else Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
                Button(
                    onClick = {
                        tools.notifyMe(Pair(getSuper(), "skip"))
                    },
                    modifier = Modifier
                        .weight(1f, true)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(buttonRoundedCorner),
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = Color.Black
                    )
                ) {
                    Icon(
                        Icons.Default.Done,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
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
                        tools.notifyMe(Pair(getSuper(), "previousState"))
                    },
                    modifier = Modifier
                        .weight(1f, true)
                        .fillMaxWidth()
                        .fillMaxHeight(),
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
                        tools.notifyMe(Pair(getSuper(), "nextState"))
                    },
                    modifier = Modifier
                        .weight(1f, true)
                        .fillMaxWidth()
                        .fillMaxHeight(),
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

    private fun getSuper() = this
}