import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import logic.Graph
import logic.Tools
import mu.KotlinLogging
import org.apache.log4j.BasicConfigurator
import ui.window.Canvas
import ui.window.footer
import ui.window.toolbar

val logger = KotlinLogging.logger {}

fun main() = singleWindowApplication(
    state = WindowState(size = DpSize(600.dp, 600.dp)),
    title = "Graph algo solver") {
    BasicConfigurator.configure()
    val graph = Graph()
    val tools = Tools(graph = graph)
    val canvas = Canvas(tools = tools, graph = graph)
    tools.canvas = canvas
    val toolbarWidth = 0.15f
    val canvasHeight = 0.75f
    Row {
        toolbar(
            modifier = Modifier
                .fillMaxHeight()
                .weight(toolbarWidth, true)
            ,
            tools = tools,
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
            footer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f - canvasHeight, true)
            )
        }
    }
}