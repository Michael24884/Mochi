package com.izanaminightz.mochi.android.ui.composables

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import coil.compose.rememberImagePainter
import com.izanaminightz.mochi.android.R
import com.izanaminightz.mochi.domain.models.Datum
import com.izanaminightz.mochi.domain.models.RelationshipType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeatureList(
    title: String,
    data: List<Datum>,
    onSelectedManga: (String) -> Unit,
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) { listState.disableScrolling(scope) }

    Column(
        Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(title, fontSize = 21.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 30.dp, start = 8.dp, top = 10.dp, end = 12.dp))
            TextButton(onClick = { /*TODO*/ }) {
                Text(stringResource(id = R.string.view_more))
            }
        }

//        LazyRow(state = listState, modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 8.dp), ) {
//            items(items = data) { items ->
//
//                val art = items.relationships?.firstOrNull { it.type == RelationshipType.Cover_Art }?.attributes?.fileName
//                    var getCover: String? = if (art != null) "https://uploads.mangadex.org/covers/${items.id}/$art.512.jpg" else null
//
//                Feature_List_Cell(title = items.attributes?.title?.en ?: items.attributes?.title?.ja ?: "Title unavailable", image = getCover, id = items.id!!, onSelected = onSelectedManga)
//            }
//        }

        FlowRow(
            alignment = Alignment.CenterHorizontally,
//            cells = GridCells.Fixed(3),
//
//            state = listState,
//            modifier = Modifier
//
//                .fillMaxSize()
//
//                ,
//            contentPadding = PaddingValues(horizontal = 8.dp)
            ) {

            data.map { items ->
                val art = items.relationships?.firstOrNull { it.type == RelationshipType.Cover_Art }?.attributes?.fileName
                    var getCover: String? = if (art != null) "https://uploads.mangadex.org/covers/${items.id}/$art.512.jpg" else null

                Feature_List_Cell(title = items.attributes?.title?.en ?: items.attributes?.title?.ja ?: "Title unavailable", image = getCover, id = items.id!!, onSelected = onSelectedManga)
            }
        }
    }
}

@Composable
fun Feature_List_Cell(
    title: String,
    image: String?,
    id: String,
    onSelected: (String) -> Unit,
) {

    Column(
        Modifier
            .size(
                width = 125.dp,
                height = 255.dp,

                )
            .padding(horizontal = 4.dp)

    ) {
        Card(elevation = 8.dp, modifier = Modifier
            .height(165.dp)
            .fillMaxWidth()
            .clickable {
            onSelected(id)
        }) {
            Image(
                painter = rememberImagePainter(image),
                contentDescription = id + "_image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4.dp))
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(title, fontSize = 15.sp, maxLines = 3, )
    }
}
