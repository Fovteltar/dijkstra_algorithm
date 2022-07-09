package application.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import androidx.compose.ui.zIndex
import application.controller.Tools
import application.ui.objects.GraphUI
import application.ui.window.Canvas
import application.ui.window.Footer
import application.ui.window.Toolbar

class UI(private val tools: Tools) {
    private val windowState = WindowState(size = DpSize(600.dp, 600.dp))
    val centerCanvasSize: Offset
        get() = Offset(
            x = windowState.size.width.value * (1f - toolbarWidth) / 2f,
            y = windowState.size.height.value * canvasHeight / 2f
        )
    val graphUI = GraphUI(tools.logic.graph)
    val toolbar = Toolbar(tools)
    private val canvas = Canvas(tools)
    private val footer = Footer(tools)

    private val toolbarWidth = 0.15f
    private val canvasHeight = 0.75f
    fun draw() {
        singleWindowApplication(
            state = windowState,
            title = "Graph algo solver",
            onKeyEvent = {
                tools.notifyMe(Pair(this, it))
                true
            }
        ) {
            Row {
                toolbar.draw(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(toolbarWidth, true)
                        .zIndex(2f)
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.00125f, true)
                        .background(color = Color.Black)
                        .zIndex(2f)
                )
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f - toolbarWidth, true)
                        .zIndex(1f)
                ) {
                    canvas.draw(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(canvasHeight, true)
                            .zIndex(1f)
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.0025f, true)
                            .background(color = Color.Black)
                            .zIndex(2f)
                    )
                    footer.draw(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f - canvasHeight, true)
                            .zIndex(2f)
                    )
                }
            }
        }
    }
}