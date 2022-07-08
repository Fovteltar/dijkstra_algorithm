package application.ui.window

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import application.controller.Tools
import logger

class Canvas(val tools: Tools) {
    private fun getSuper() = this
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun draw(modifier: Modifier) {
        val verticesAmount = remember { tools.verticesAmount }
        val edgesAmount = remember { tools.edgesAmount }
        val canvasOffset = remember { mutableStateOf(Offset.Zero) }
        var oldScale = 1f
        val scale = remember { mutableStateOf(1f) }
        Box(
            modifier = Modifier
                .onPointerEvent(PointerEventType.Scroll) {
                    val sign = if (it.changes.first().scrollDelta.y > 0) "+" else "-"
                    val zoom = 0.1f
                    if (sign == "+" && scale.value >= 0.1f) {
                        oldScale = scale.value
                        scale.value -= zoom
                    }
                    else if (sign == "-" && scale.value < 3f) {
                        oldScale = scale.value
                        scale.value += zoom
                    }
                    logger.info("[Tools] scale: ${scale.value}")
                }
                .background(color = Color.White)
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, zoom, rotation ->
                        canvasOffset.value += pan
//                        canvasOffset.value = (canvasOffset.value + centroid / oldScale) -
//                                (centroid / scale.value + pan / oldScale)
//                        logger.info("""
//                            [Canvas] detectTransformGestures:
//                                offset = ${canvasOffset.value}
//                                scale = ${scale.value}
//                        """.trimIndent())
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        logger.info("[Canvas] detectTapGestures: offset = $offset")
                        tools.notifyMe(Pair(getSuper(), offset - canvasOffset.value))
                    }
                }
                .graphicsLayer {
                    translationX = canvasOffset.value.x
                    translationY = canvasOffset.value.y
                    scaleX = scale.value
                    scaleY = scale.value
                }
                .composed { modifier }
        ) {
        logger.info("[CANVAS]: BOX WAS UPDATED!")

            if (verticesAmount.value > 0) {
                for (vertexUI in tools.ui?.graphUI?.verticesUI!!.keys) {
                    vertexUI.draw()
                }
            }

            if (edgesAmount.value > 0) {
                for (edgeUI in tools.ui?.graphUI?.getEdges()!!) {
                    edgeUI.draw()
                }
            }
        }
    }
}
