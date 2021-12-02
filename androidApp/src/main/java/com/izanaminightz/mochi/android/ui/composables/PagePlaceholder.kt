package com.izanaminightz.mochi.android.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ScrollingDirection {
    Left,
    Right,

}

@Composable
fun PagePlaceholder(
    nextChapter: Int,
    scrollingDirection: ScrollingDirection,
    isDark: Boolean = false,
    onNextPage: () -> Unit,
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(if (isDark) Color.White else Color.Black)
    ) {
       Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
           Text(text = "End of chapter ${nextChapter - 1}",
                    fontWeight = FontWeight.Bold,
               fontSize = 18.sp,
               textAlign = TextAlign.Center,
               color = Color.White
               )

           Button(onClick = onNextPage, modifier = Modifier.padding(top = 15.dp)) {
               Text("Continue to chapter ${nextChapter+1}")
           }
       }
    }
}