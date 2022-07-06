package application.ui.objects

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import application.controller.Tools
import application.logic.Vertex

const val VERTEX_SIZE = 100

class VertexUI(val vertex: Vertex = Vertex(), private val tools: Tools) {
    var topLeftOffset: Offset = Offset(0f, 0f)

    @Composable
    fun drawVertex() {
        Canvas(
            modifier = Modifier
                .offset(x = topLeftOffset.x.dp, y = topLeftOffset.y.dp)
                .size(VERTEX_SIZE.dp, VERTEX_SIZE.dp)
                .clip(shape = CircleShape)
                .clickable {
                    tools.notifyMe(sender = this)
                },
        ) {
            drawCircle(Color.Blue, VERTEX_SIZE / 2f)
        }
    }
}