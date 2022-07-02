package ui.window

import androidx.compose.foundation.background
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun footer(
    modifier: Modifier,
) {
    Text(
        text = "Пошаговый вывод программы/Пояснения",
        textAlign = TextAlign.Center,
        color = Color.Black,
        modifier = Modifier
            .composed { modifier }
            .background(color = Color.Gray)
    )
}