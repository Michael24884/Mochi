package com.izanaminightz.mochi.android.ui.view

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.izanaminightz.mochi.android.ui.viewModel.ListScreenViewModel
import com.izanaminightz.mochi.presentation.mylist.ListModel

@Composable
fun ListScreen(
    navController: NavController,

) {
    val viewModel: ListScreenViewModel = viewModel()
    val state by viewModel.list.observeAsState()

    when(state) {
        is ListModel.State -> {
            val data = (state as ListModel.State)
            print(data.list)
            Log.i("Success", data.list.statuses.toString())
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "My List")
                }
            )
        }
    ) {
        Column {

        }
    }
}