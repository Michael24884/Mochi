package com.izanaminightz.mochi.android.ui.view

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.izanaminightz.mochi.android.R
import com.izanaminightz.mochi.android.ui.composables.MochiTextField
import com.izanaminightz.mochi.android.ui.viewModel.SearchViewModel
import com.izanaminightz.mochi.domain.models.Datum
import com.izanaminightz.mochi.domain.models.MangadexResultsModel
import com.izanaminightz.mochi.domain.models.RelationshipType
import com.izanaminightz.mochi.presentation.search.SearchModel
import com.izanaminightz.mochi.utils.MangadexHelper
import kotlinx.coroutines.runBlocking

@Composable
fun SearchScreen(
    navController: NavController
) {

    val searchViewModel: SearchViewModel = viewModel()
    val results by searchViewModel.results.observeAsState()



    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Sharp.ArrowBack, "Go Back")
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.search))
                }

            )
        }

    ) {

        Column {
            MochiTextField(
                onSearch = {
                    runBlocking {
                        searchViewModel.searchFor(it)
                    }
                }
            )

            when(results) {
                is SearchModel.Idle -> Box {

                }
                is SearchModel.Loading -> Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                }
                is SearchModel.State -> {
                    val results = (results as SearchModel.State).results
                    Main_Body(results) { navController.navigate("Detail/$it") }
                }
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Main_Body(
    results: MangadexResultsModel,
    onTap: (String) -> Unit
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        contentPadding = PaddingValues(all = 8.dp)
    ) {
        items(items = results.data ?: listOf()) { item ->
            Results_Cells(item = item, onTap = onTap)
        }
    }
}



@Composable
private fun Results_Cells(
    item: Datum,
    onTap: (String) -> Unit,
) {
    Column(
        Modifier.height(230.dp)
    ){
        Card(
            Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                .height(160.dp)
                .clickable {
                    onTap(item.id!!)
                },

            elevation = 5.dp,
            ) {
                val art = item.relationships?.firstOrNull { it.type == RelationshipType.Cover_Art }?.attributes?.fileName
                Image(
                    rememberImagePainter(MangadexHelper().coverCreator(item.id!!, art)),
                    contentDescription = "Cover_art_$art",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.size(150.dp, 150.dp)
                )
        }

        Text(item.attributes?.title?.en ?: "N/A", maxLines = 3, fontSize = 15.sp, overflow = TextOverflow.Ellipsis)
    }
}
