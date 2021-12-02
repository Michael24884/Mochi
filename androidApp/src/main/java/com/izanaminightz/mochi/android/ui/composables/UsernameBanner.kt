package com.izanaminightz.mochi.android.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.izanaminightz.mochi.android.*
import com.izanaminightz.mochi.utils.MochiHelper

@Composable
fun UsernameBanner() {

    LaunchedEffect(key1 = Unit ) {
        if (MainApplication.user == null)
            MochiHelper().getUser()
    }

    if (MainApplication.user != null)
       Row(horizontalArrangement = Arrangement.Start) {
           Text(
               text = "Welcome Back, ",
               fontWeight = FontWeight.ExtraBold,
               fontSize = 20.sp,
               modifier = Modifier.padding(all = 8.dp)
           )
           Text(
               text = "${MainApplication.user?.username}",
               fontWeight = FontWeight.ExtraBold,
               color = colorResource(id = R.color.colorPrimaryDark),
               fontSize = 20.sp,
               modifier = Modifier.padding(all = 8.dp)
           )
       }
}