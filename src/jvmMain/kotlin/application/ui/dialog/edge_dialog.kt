package application.ui.dialog

import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Dialog
import logger

class EdgeDialog {
    val textState = mutableStateOf("1")

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
            ) {
                TextField(
                    value = text.value,
                    onValueChange = {
                        try {
                            it.toUInt()
                            text.value = it
                            switcher.value = !switcher.value
                        }
                        catch (exc: Exception) {
                            logger.error("[EdgeDialog] ${exc.message}")
                        }
                    }
                )
            }
        }
    }
}