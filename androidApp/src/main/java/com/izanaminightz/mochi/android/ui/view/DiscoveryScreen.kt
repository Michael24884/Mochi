package com.izanaminightz.mochi.android.ui.view

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.izanaminightz.mochi.android.R
import com.izanaminightz.mochi.android.ui.composables.FeatureList
import com.izanaminightz.mochi.android.ui.composables.Feature_Card
import com.izanaminightz.mochi.android.ui.viewModel.MangadexHomeViewModel
import org.koin.androidx.compose.getViewModel
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.izanaminightz.mochi.android.ui.viewModel.DiscoveryViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import com.izanaminightz.mochi.android.ui.composables.UsernameBanner
import com.izanaminightz.mochi.presentation.discovery.DiscoveryModel


@Composable
fun DiscoveryScreen(
    navController: NavController
) {
    val viewModel: DiscoveryViewModel = viewModel()
    val discoveryState by viewModel.homeData.observeAsState()


    Scaffold(
        topBar = {
            TopAppBar(
                actions = {
                  IconButton(
                      onClick = {
                                navController.navigate("Search")
                      },
                      modifier = Modifier.padding(end = 8.dp),
                      ) { Icon(Icons.Filled.Search,
                      "Search") }
                },
                title = {
                    Text(stringResource(R.string.discovery))
                }
            )
        }
    ) {
       when (discoveryState) {
           is DiscoveryModel.LoadingDiscoveryState -> Box(){}
           is DiscoveryModel.Error -> {
               val error = discoveryState as DiscoveryModel.Error
               
               AlertDialog(onDismissRequest = { /*TODO*/ },
                   title = {Text("An error has occured")},
                   text = {Text(error.error)},
                    buttons = {
                        TextButton(onClick = { /*TODO*/ }) {
                            Text("Dismiss")
                        }
                    }
                   )
           }
           is DiscoveryModel.DiscoveryState -> {
               val recentManga = (discoveryState as DiscoveryModel.DiscoveryState).recentlyAddedManga
               Column(
                   Modifier
                       .padding(it)
                       .verticalScroll(
                           rememberScrollState()
                       )
               ) {

                    UsernameBanner()

                   FeatureList(
                       title = stringResource(R.string.just_added),
                       data = recentManga ?: listOf(),
                       onSelectedManga = { id -> navController.navigate("Detail/$id") }
                   )

//                   if (randomMangaList.isNotEmpty())
//                       Feature_Card(data = randomMangaList.first())

               }
           }
       }
    }

}

@Composable
private fun MainContent() {

}