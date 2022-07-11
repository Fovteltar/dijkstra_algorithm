package application.ui.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import logger

class EdgeDialog {
    val textState = mutableStateOf("1")

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun draw(isDialogOpen: MutableState<Boolean>, switcher: MutableState<Boolean>) {
        println("TEXTSTATE: ${textState.value}")
        val text = remember { textState }
        val isDialogOpen = remember { isDialogOpen }
        if (isDialogOpen.value) {
            Dialog(
                onCloseRequest = {
                    isDialogOpen.value = false
                },
                onKeyEvent = {
                    when(it.key) {
                        Key.Escape, Key.Enter -> {
                            isDialogOpen.value = false
                            true
                        }
                        else -> {
                            false
                        }
                    }
                },
                title = "Enter edge weight",
                resizable = false
            ) {
                TextField(
                    modifier = Modifier.padding(50.dp, 75.dp),
                    value = text.value,
                    onValueChange = {
                        try {
                            if (it.length <= 3) {
                                it.toUInt()
                                text.value = it
                                switcher.value = !switcher.value
                            }
                        }
                        catch (exc: Exception) {
                            logger.error("${exc.message}")
                        }
                    }
                )
            }
        }
    }
}