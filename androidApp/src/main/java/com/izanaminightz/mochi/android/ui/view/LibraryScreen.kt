package com.izanaminightz.mochi.android.ui.view

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.izanaminightz.mochi.android.ui.composables.ListCard
import com.izanaminightz.mochi.android.ui.viewModel.ListScreenViewModel
import com.izanaminightz.mochi.domain.models.Datum
import com.izanaminightz.mochi.presentation.mylist.ListModel

@Composable
fun ListScreen(
    navController: NavController,

) {
    val viewModel: ListScreenViewModel = viewModel()
    val state by viewModel.list.observeAsState()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "My Library")
                }
            )
        }
    ) {

            when(state) {
                is ListModel.State -> {
                    val data = (state as ListModel.State)
                    MainContent(data.list) { data ->
                        val id = data.id
                        navController.navigate("Detail/$id")
                    }

                }
            }

    }
}

@Composable
private fun MainContent(
    data: List<Datum>,
    onSelected: ((Datum) -> Unit)
) {
   LazyColumn() {
       items(items = data) { list ->
           ListCard(list) {
               onSelected(list)
           }
       }
   }
}