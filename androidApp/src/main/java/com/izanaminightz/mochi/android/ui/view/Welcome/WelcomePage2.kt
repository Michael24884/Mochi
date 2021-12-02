package com.izanaminightz.mochi.android.ui.view.Welcome

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.izanaminightz.mochi.presentation.auth.AuthHandler
import com.izanaminightz.mochi.presentation.auth.AuthState
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun WelcomePage2(
    navController: NavController
){

    val authRep = AuthHandler()

    var isFetching by remember {
        mutableStateOf(false)
    }

    var userNameText by remember {
        mutableStateOf(TextFieldValue())
    }

    var passwordText by remember {
        mutableStateOf(TextFieldValue())
    }

    var error by remember {
        mutableStateOf<String?>(null)
    }


    suspend fun login() {
        if (error != null) error = null

        val state = authRep.login(
            userNameText.text,
            passwordText.text,
        ) {
           it?.let {
               val json = Json.encodeToString(it)
                navController.navigate("LoggedInPage/user=$json") {
                    popUpTo(0)
                    launchSingleTop = true


                }
           }
        }


        isFetching = false
        when(state) {
            is AuthState.AuthError -> error = state.error
            is AuthState.AuthSuccess -> {
                //Save tokens
            }
        }


    }



    Scaffold {
        Column(Modifier.padding(all = 12.dp)) {
            Spacer(Modifier.height(20.dp))

            Text("Sign In to MangaDex",
                fontWeight = FontWeight.Black,
            fontSize = 30.sp
                )
            Text("Sync your lists on Mangadex and view your followed feed",
                fontSize = 17.sp,
                color = Color.Gray)


            Spacer(Modifier.height(90.dp))

            Column(
                Modifier
                    .fillMaxHeight(0.7f)
                    .fillMaxWidth()) {

                TextField(
                    value = userNameText,
                    onValueChange = { userNameText = it },
                    placeholder = { Text("Username or Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = error != null,
                    )

                Spacer(modifier = Modifier.height(25.dp))
                
                TextField(
                    value = passwordText,
                    onValueChange = { passwordText = it },
                    placeholder = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = error != null,
                )
                Spacer(modifier = Modifier.height(15.dp))
                if (error != null)
                    Text(error!!, color = Color.Red, fontSize = 15.sp, fontWeight = FontWeight.Medium)

            }
            
            
            Spacer(Modifier.fillMaxHeight(0.35f))

            Button(
                enabled = (userNameText.text.isNotEmpty() && passwordText.text.isNotEmpty()) && !isFetching,
                onClick = {
                    isFetching = true
                    runBlocking { login() }
            }, modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)) {
                if (isFetching)
                    CircularProgressIndicator()
                else
                Text("Log In", fontWeight = FontWeight.Black)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)) {
                TextButton(onClick = { navController.navigate("Welcome/3") { launchSingleTop = true } }) {
                    Text("Skip for now", fontWeight = FontWeight.Bold)
                }
            }

        }
    }
}
//
//@Preview
//@Composable
//private fun Preview() {
//    WelcomePage2()
//}