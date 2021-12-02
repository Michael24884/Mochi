package com.izanaminightz.mochi.android.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.izanaminightz.mochi.android.ui.viewModel.MangadexHomeViewModel
import com.izanaminightz.mochi.utils.MangadexHelper
import org.koin.androidx.compose.getViewModel
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.izanaminightz.mochi.android.R
import com.izanaminightz.mochi.android.ui.viewModel.DetailViewModel
import com.izanaminightz.mochi.android.ui.viewModel.MangadexDetailViewModel
import com.izanaminightz.mochi.domain.models.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import com.izanaminightz.mochi.android.ui.composables.FlowRow
import com.izanaminightz.mochi.android.ui.composables.Pill
import com.izanaminightz.mochi.presentation.detail.DetailModel
import com.izanaminightz.mochi.presentation.reader.ReaderDataModel
import kotlinx.serialization.Serializable

@Composable
fun DetailScreen(
    navController: NavController,
    mangaID: String
) {
    val detailViewModel: DetailViewModel = viewModel()
    val detailState by detailViewModel.manga.observeAsState()


    LaunchedEffect(key1 = Unit ) {
        detailViewModel.fetchManga(mangaID)
    }

    when(detailState) {
        is DetailModel.Loading -> CircularProgressIndicator()
        is DetailModel.State -> {
            val data = (detailState as DetailModel.State)
            Main_Content(navController = navController, data = data.detail, feed = data.feed)
        }
    }

}

@Composable
private fun Main_Content(
    navController: NavController,
    data: MangadexResultsSingleModel,
    feed: MangadexMangaFeed?
) {
     var synopsisExpanded by rememberSaveable {
         mutableStateOf(false)
     }
    
    
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Sharp.ArrowBack, "Go Back")
                    }
                },
                title = {"Detail"}

            )
        }
    ) {

        val coverArt = data.data.relationships?.firstOrNull { it.type == RelationshipType.Cover_Art }?.attributes?.fileName

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
            ) {
            Box(Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Card(elevation = 16.dp, modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 40.dp)) {
                        Image(
                            painter = rememberImagePainter(MangadexHelper().coverCreator(data.data.id!!, coverArt)),
                            contentDescription = "cover art",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .size(width = 150.dp, height = 215.dp)
                                .clip(RoundedCornerShape(6.dp))
                        )
                    }

                    Text(data.data.attributes?.title?.en ?: "Title unavailable", modifier = Modifier.padding(top = 11.dp, start = 8.dp, end = 8.dp), fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

                    if (data.data.attributes?.altTitles?.firstOrNull { it.en != null} != null)
                        Text(data.data.attributes?.altTitles?.last { it.en != null }!!.en ?: "", fontSize = 20.sp, fontWeight = FontWeight.Medium, color = colorResource(R.color.softGray), textAlign = TextAlign.Center, modifier = Modifier.padding(top = 6.dp, start = 8.dp, end = 8.dp))

                      data.data.relationships?.firstOrNull { it.type == RelationshipType.Author }?.attributes?.name?.let {
                         Text(it, fontSize = 14.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center, color = Color(0xFF8A8A8A), modifier = Modifier.padding(top = 8.dp))
                     }

                    Spacer(modifier = Modifier.padding(bottom = 10.dp,))

                    Row(
                        Modifier

                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp, top = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(
                            enabled = (feed?.data?.isNotEmpty() ?: false),
                            onClick = {
                            if (feed?.data?.isNullOrEmpty() == true) return@Button;
                            feed?.data?.firstOrNull().let {
                                val readerData = ReaderDataModel(
                                    hash = it!!.attributes.hash,
                                    chapterID = it.id,
                                    pages = it.attributes.pages,
                                    chapter = it.attributes.chapter ?: "1"
                                )


                                navController.currentBackStackEntry?.arguments?.putSerializable("chapters", readerData)
                                navController.navigate("Reader")

                            }
                                         }, modifier = Modifier
                            .fillMaxWidth(0.68f)
                            .height(46.dp)

                            ) {
                            if (feed == null)
                                CircularProgressIndicator(color = Color.White)
                            else if (feed.result == "ok" && feed.data.isEmpty())
                                Text(stringResource(id = R.string.no_chapters))
                            else
                                Text("${stringResource(R.string.read_chapter___)} ${feed.data.first().attributes.chapter ?: "1"}", fontWeight = FontWeight.Black, fontSize = 16.sp)
                        }

                        Button(
                            onClick = { /*TODO*/ },
                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.softBlue)),
                            modifier = Modifier
                                .fillMaxWidth(0.45f)
                                .height(46.dp)
                        ) {
                            Icon(Icons.Outlined.Favorite, "Save to Library")
                        }


                        Button(onClick = { /*TODO*/ },
                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.notification)),
                            modifier = Modifier
                                .fillMaxWidth(0.75f)
                                .height(46.dp)
                                ) {
                            Icon(Icons.Outlined.Notifications, "Notification")
                        }





                    }

                    if (feed?.data?.isNullOrEmpty() == false)
                        TextButton(onClick = {
//                            val json = Json.encodeToString(feed)
                            navController.navigate("Feed/${data.data.id}") }, modifier = Modifier.padding(top = 6.dp)) {
                            Text(stringResource(id = R.string.view_all_chapters))
                        }


                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp)) {
                        Text(stringResource(R.string.description), fontWeight = FontWeight.Medium, fontSize = 22.sp)
                        TextButton(onClick = { synopsisExpanded = !synopsisExpanded }) {
                            Text(text = if (synopsisExpanded) stringResource(id = R.string.read_less) else stringResource(id = R.string.read_more))
                        }
                    }

                    //Description
                    val description = data.data.attributes?.description

                    Text(if (description is ArrayValue) "No synopsis currently found" else (description as DescriptionClass).en ?: "No synopsis currently found",
                        color = colorResource(R.color.softGray),
                        modifier = Modifier.padding(horizontal = 8.dp),
                        maxLines = if (synopsisExpanded) 450 else 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    //Genres
                    val genres = data.data.attributes?.tags
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)) {
                        Text(stringResource(id = R.string.genres_and_tags), fontWeight = FontWeight.Medium, fontSize = 22.sp, modifier = Modifier.padding(bottom = 5.dp, top = 6.dp))
                        when(genres) {
                            is TagArrayValue ->
                                FlowRow {
                                    genres.value.map {
                                        Pill(name = it.attributes?.name?.en ?: "N/A")
                                    }
                                }
                            is TagMapValue -> Box(){}
                        }
                    }

                }
            }
        }
    }


}
