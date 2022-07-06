package application.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import application.controller.Tools
import application.ui.objects.GraphUI
import application.ui.window.Canvas
import application.ui.window.Footer
import application.ui.window.Toolbar

class UI(private val tools: Tools) {
    val graphUI = GraphUI(tools.logic.graph)
    val toolbar = Toolbar(tools)
    private val canvas = Canvas(tools)
    private val footer = Footer(tools)
    fun draw() {
        singleWindowApplication(
            state = WindowState(size = DpSize(600.dp, 600.dp)),
            title = "Graph algo solver",
            onKeyEvent = {
                tools.notifyMe(Pair(this, it))
                true
            }
        ) {
            val toolbarWidth = 0.15f
            val canvasHeight = 0.75f
            Row {
                toolbar.draw(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(toolbarWidth, true),
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.00125f, true)
                        .background(color = Color.Black)
                )
                Column(
                    modifier = Modifier
                        .weight(1f - toolbarWidth, true)
                ) {
                    canvas.draw(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(canvasHeight, true)
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.0025f, true)
                            .background(color = Color.Black)
                    )
                    footer.draw(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f - canvasHeight, true)
                    )
                }
            }
        }
    }
}