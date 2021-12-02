package com.izanaminightz.mochi.android.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.izanaminightz.mochi.android.R
import com.izanaminightz.mochi.android.ui.viewModel.FeedViewModel
import com.izanaminightz.mochi.domain.models.MangadexFeedData
import com.izanaminightz.mochi.domain.models.RelationshipType
import com.izanaminightz.mochi.presentation.feed.FeedModel
import com.izanaminightz.mochi.presentation.reader.ReaderDataModel
import com.izanaminightz.mochi.utils.MangadexHelper

@Composable
fun FeedScreen(
    navController: NavController,
    mangaID: String,
) {

    val viewModel: FeedViewModel = viewModel()
    val state by viewModel.feed.observeAsState()


    LaunchedEffect(key1 = mangaID) { viewModel.fetchFeed(mangaID) }

    when (state) {
        is FeedModel.Loading -> {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
            }
        }
        is FeedModel.Error -> {
            Box() {}
        }
        is FeedModel.State -> {
            val feed = (state as FeedModel.State).feed.data

            Scaffold(
                topBar = {
                    TopAppBar(
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Sharp.ArrowBack, "Go Back")
                            }
                        },
                        title = {
                            Text("${feed.count()} Chapters")
                        }
                    )
                }
            ) {
                LazyColumn() {
                    items(feed) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val read = ReaderDataModel(
                                        chapter = it.attributes.chapter ?: "",
                                        pages = it.attributes.pages,
                                        chapterID = it.id,
                                        hash = it.attributes.hash,
                                    )
                                    navController.currentBackStackEntry?.arguments?.putSerializable("chapters", read)
                                    navController.navigate("Reader")
                                }
                                .padding(horizontal = 8.dp, vertical = 8.dp)

                        ) {
                            Column(
                                Modifier
                                    .height(56.dp)
                                    .padding(3.dp), verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "Chapter ${it.attributes.chapter}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )


                                    Text(
                                        MangadexHelper().timeDifference(it.attributes.createdAt),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    it.attributes.title.let {
                                        Text(
                                            it ?: "", fontSize = 12.sp,
                                            color = colorResource(
                                                id = R.color.softGray
                                            ),
                                        )
                                    }

                                    val scanGroupName = it.relationships.firstOrNull { it.type == RelationshipType.Scanlation }?.attributes?.name ?: "N/A"

                                    Text(
                                        scanGroupName,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = colorResource(
                                            id = R.color.softGray
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}