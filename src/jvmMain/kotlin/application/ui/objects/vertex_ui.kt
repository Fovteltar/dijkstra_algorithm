package application.ui.objects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import application.controller.Tools
import logic.Vertex

const val VERTEX_SIZE = 100

class VertexUI(val vertex: Vertex = Vertex(), var topLeftOffset: Offset = Offset(0f, 0f), private val tools: Tools) {
    var isAlgoStartedState = mutableStateOf(false)
    var weightInAlgorithmState = mutableStateOf("")
    var colorState = mutableStateOf(VertexColor.DEFAULT)
    @Composable
    fun draw() {
        var isAlgoStarted = remember { isAlgoStartedState }
        var weightInAlgorithm = remember { weightInAlgorithmState }
        var color = remember { colorState }
        if (isAlgoStarted.value) {
            Text(
                text = weightInAlgorithm.value,
                color = Color.Black,
                fontSize = 26.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset(x = (topLeftOffset.x + VERTEX_SIZE / 4).dp, y = (topLeftOffset.y - 3 * VERTEX_SIZE / 4).dp)
                    .size((VERTEX_SIZE / 2).dp, (VERTEX_SIZE / 2).dp)
            )
        }
        Surface(
            modifier = Modifier
                .offset(x = topLeftOffset.x.dp, y = topLeftOffset.y.dp)
                .size(VERTEX_SIZE.dp, VERTEX_SIZE.dp)
                .clip(shape = CircleShape)
                .clickable {
                    tools.notifyMe(sender = this)
                },
            shape = CircleShape,
            color = color.value.color,
        ) {
            Text(
                text = let {
                    try {
                        vertex.vertexName.toInt()
                        ""
                    }
                    catch(exc: Exception) {
                        vertex.vertexName
                    }
                },
                color = Color.Black,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset(x = (VERTEX_SIZE / 4).dp, y = (VERTEX_SIZE / 4).dp,)
                    .size((VERTEX_SIZE / 2).dp, (VERTEX_SIZE / 2).dp)
            )
        }
    }
}