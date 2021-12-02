package com.izanaminightz.mochi.android.ui.view.Welcome

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.izanaminightz.mochi.domain.models.UserModel
import java.util.*
import kotlin.concurrent.schedule
import com.izanaminightz.mochi.android.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoggedInPage(
    navController: NavController,
    user: UserModel,
) {

    var state by remember {
        mutableStateOf(false)
    }


    LaunchedEffect(key1 = Unit) {
        Timer("Start", false).schedule(450) {state = true}
    }


    Scaffold {
        Column(
            Modifier
                .padding(all = 12.dp)
                .fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {



          Column() {
              Spacer(Modifier.height(75.dp))

              AnimatedVisibility(
                  visible = true,
                  enter = fadeIn() + slideInVertically()
              ) {
                  Text(text = "You're logged in as", fontWeight = FontWeight.Normal, fontSize = 20.sp, color = Color.Gray)
              }

              AnimatedVisibility(visible = state, enter = slideInHorizontally()) {
                  Text(user.username, fontWeight = FontWeight.Black, fontSize = 45.sp)
              }
          }

            Text("You're now able to sync your lists and will be able to view updates", fontSize = 15.sp, textAlign = TextAlign.Center, color = colorResource(
                id = R.color.softGrayText
            ))

            Button(onClick = { navController.navigate("Welcome/3") { launchSingleTop = true } }, modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .padding(horizontal = 8.dp)) {
                Text("Continue", fontWeight = FontWeight.Bold)
            }
        }
    }
}