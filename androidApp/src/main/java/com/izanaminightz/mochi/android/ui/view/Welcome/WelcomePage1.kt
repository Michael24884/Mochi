package com.izanaminightz.mochi.android.ui.view.Welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.izanaminightz.mochi.android.R
import java.util.*
import java.util.logging.Handler
import kotlin.concurrent.schedule


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WelcomePage1(
    navController: NavController
) {

    var visible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        Timer("Start", false).schedule(350) {visible = true}
    }

    Scaffold {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,

            modifier = Modifier.fillMaxSize()
        ) {
            
            Spacer(modifier = Modifier.height(50.dp))

            AnimatedVisibility(
                visible,
            enter = slideInVertically() + fadeIn()
                ) {
                Image(
                    painterResource(id = R.drawable.mochi_image),
                    contentDescription = "Logo",
                    Modifier
                        .clip(
                            CircleShape
                        )
                        .size(250.dp, 250.dp)

                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            AnimatedVisibility(visible) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Welcome to Mochi",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black
                    )

                    Text("An unofficial Mangadex app",
                        color = Color.Gray,
                        fontSize = 16.sp,

                        )
                }
            }

            Spacer(Modifier.fillMaxHeight(0.8f))

            Button(onClick = { navController.navigate("Welcome/2") }, modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .padding(horizontal = 8.dp)) {
                Text("Let's get started!", fontWeight = FontWeight.Bold)
            }

        }
    }
    
}



/**
 * Intro
 * Sign in
 * Language
 * Filter
 * Discord
 */