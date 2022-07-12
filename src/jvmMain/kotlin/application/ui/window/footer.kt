package application.ui.window

import androidx.compose.foundation.background
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import application.controller.Tools

class Footer(val tools: Tools) {
    val textState = mutableStateOf("Задайте граф и запустите алгоритм")
    @Composable
    fun draw(modifier: Modifier) {
        val text = remember { textState }
        Text(
            text = textState.value,
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = 30.sp,
            modifier = Modifier
                .composed { modifier }
                .background(color = Color.Gray)
        )
    }
}