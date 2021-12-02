package com.izanaminightz.mochi.android.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.izanaminightz.mochi.android.R
import com.izanaminightz.mochi.android.ui.theme.MochiTheme

@Composable
fun Pill(
    name: String,
) {
    Box(
        Modifier
            .padding(all = 5.dp)
            .clip(RoundedCornerShape(25.dp))

    ) {
       Box(
           Modifier.background(
            colorResource(id = android.R.color.holo_purple)
           )
       ) {
           Text(text = name,
               fontWeight = FontWeight.Medium,
               fontSize = 16.sp,
               color = Color.White,
               modifier = Modifier.padding(all = 10.dp)
           )
       }
    }
}

