package com.izanaminightz.mochi.android.ui.view.Welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.izanaminightz.mochi.android.R
import com.izanaminightz.mochi.android.Screens
import com.izanaminightz.mochi.domain.models.MangadexLanguages
import com.izanaminightz.mochi.utils.MochiHelper


@Composable
fun WelcomePage3(
    navController: NavController
) {

    var selectedLanguage by remember {
        mutableStateOf<MangadexLanguages>(MangadexLanguages.en)
    }

    @Composable
    fun languageCell(language: MangadexLanguages) {
        Language_Cells(language, isSelected = language == selectedLanguage) { selectedLanguage  = language }
    }

    Scaffold {
        Column(Modifier.padding(all = 12.dp)) {
            Spacer(modifier = Modifier.height(20.dp))
            
            Text(text = "Select your preferred language",
            fontWeight = FontWeight.Black,
                fontSize = 30.sp
                )
            
            Spacer(modifier = Modifier.height(100.dp))
            
            LazyColumn(Modifier.fillMaxWidth()) {
                    item {
                        MangadexLanguages.values().map { 
                            languageCell(language = it)
                        }
                    }
            }
            

            Spacer(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f ))


            Button(onClick = {
                MochiHelper().storeLanguage(selectedLanguage)
                MochiHelper().storeCompletedOnboarding()

                navController.navigate(Screens.DiscoveryScreen.title) {
                    popUpTo(0) { }
                    launchSingleTop = true
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .padding(horizontal = 8.dp)) {
                Text("Continue", fontWeight = FontWeight.Bold)
            }
        }
    }
}



@Composable
private fun Language_Cells(language: MangadexLanguages, isSelected: Boolean, onPress: (String) -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .height(45.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(
                if (isSelected) colorResource(id = R.color.colorPrimaryDark) else colorResource(
                    id = R.color.softGrayText
                )
            )
            .clickable {
                onPress(language.language)
            }
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = language.language,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}