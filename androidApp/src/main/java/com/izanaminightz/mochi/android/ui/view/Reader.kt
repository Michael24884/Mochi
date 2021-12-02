package com.izanaminightz.mochi.android.ui.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.izanaminightz.mochi.android.model.MangaChapters
import com.izanaminightz.mochi.android.ui.composables.PagePlaceholder
import com.izanaminightz.mochi.android.ui.composables.ScrollingDirection
import com.izanaminightz.mochi.android.ui.viewModel.ReaderViewModel
import com.izanaminightz.mochi.presentation.reader.ReaderDataModel
import com.izanaminightz.mochi.presentation.reader.ReaderModel
import io.ktor.util.reflect.*
import kotlinx.coroutines.runBlocking


private const val testImageURL =
    "https://uploads.mangadex.org/covers/a96676e5-8ae2-425e-b549-7f15dd34a6d8/11cbae12-ea42-4a9e-89c1-f9d515c7a936.png"

@ExperimentalPagerApi
@Composable
fun Reader(
    data: ReaderDataModel? = null,
    navController: NavController,
) {
    val viewModel: ReaderViewModel = viewModel()
    val state by viewModel.data.observeAsState()


    val pagerState = rememberPagerState()

    DisposableEffect(key1 = data) {
        if (data != null) {
            viewModel.fetchChapters(
                data
            )
        }
        onDispose {
            Log.i("Reader", "Shutting down image preloader task[s]")
            viewModel.dispose()
        }
    }


    when (state) {
        is ReaderModel.State -> {
            val state = (state as ReaderModel.State).data

            var isTapped by remember {
                mutableStateOf(false)
            }

            val currentPage = pagerState.currentPage


            LaunchedEffect(key1 = state) {
                if (state.isNotEmpty())
                    pagerState.scrollToPage(1)
            }

            //TODO: User preference background BLACK or WHITE
            Scaffold(
                Modifier.background(Color.Black)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    if (isTapped)
                        TopAppBar(
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(Icons.Sharp.ArrowBack, contentDescription = "Go Back")
                                }
                            },
                            title = { Text("Chapter ${data?.chapter ?: ""}") }
                        ) else Spacer(modifier = Modifier.height(55.dp))


                    HorizontalPager(
                        count = state.size + 2,
                        state = pagerState,
                        reverseLayout = true,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember{ MutableInteractionSource() },

                        )
                        { isTapped = !isTapped }
                            .fillMaxHeight(0.92f)
                    ) { page ->
                        when (page) {
                            0 -> PagePlaceholder(
                                nextChapter = 0,
                                scrollingDirection = ScrollingDirection.Right,
                                onNextPage = {})
                            state.size + 1 -> PagePlaceholder(
                                nextChapter = (data!!.chapter.toInt()) + 1,
                                scrollingDirection = ScrollingDirection.Left,
                                onNextPage = {

                                })
                            else -> {
                                Column(
                                    Modifier
                                        .background(Color.Black)
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        rememberImagePainter(
                                            state[page - 1],
                                            builder = {
                                                crossfade(true)
                                            }
                                        ),
                                        contentDescription = "$page",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.fillMaxSize()
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null,
                                            ) {

                                            },

                                        )
                                }
                            }
                        }
                    }

                    if (isTapped)
                        BottomAppBar(

                            Modifier.height(55.dp)
                        ) {
                            Row(
                                Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text("$currentPage / ${state.size}", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    else Spacer(modifier = Modifier.height(55.dp))

                }
            }
        }
        else -> {
            Box() {}
        }
    }


}