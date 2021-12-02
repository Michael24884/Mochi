package com.izanaminightz.mochi.android.ui.composables

import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.izanaminightz.mochi.android.ui.theme.MochiTheme
import com.izanaminightz.mochi.domain.models.ArrayValue
import com.izanaminightz.mochi.domain.models.DescriptionClass
import com.izanaminightz.mochi.domain.models.MangadexResultsSingleModel
import com.izanaminightz.mochi.domain.models.RelationshipType
import com.izanaminightz.mochi.utils.MangadexHelper


@Composable
fun Feature_Card(
    data: MangadexResultsSingleModel
) {
    Card(elevation = 3.dp, modifier = Modifier
        .height(170.dp)
        .fillMaxWidth()
        .padding(15.dp)
        ) {

        val art = data.data.relationships?.firstOrNull { it.type == RelationshipType.Cover_Art }?.attributes?.fileName
        val description = data.data.attributes?.description

        Box(modifier = Modifier.clip(
            RoundedCornerShape(8.dp)
        )) {
            Row(Modifier.fillMaxSize()) {
                Image(
                    painter = rememberImagePainter(MangadexHelper().coverCreator(data.data.id!!, art)),
                    contentDescription = art + "_image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(110.dp)
                        .padding(end = 4.dp)
                    )

                Column(Modifier.fillMaxSize().padding(4.dp), verticalArrangement = Arrangement.SpaceBetween) {
                    Text(data.data.attributes?.title?.en ?: "Title unavailable", fontWeight = FontWeight.Medium, fontSize = 18.sp)
                    Text(if (description is ArrayValue) "Description unavailable" else (description as DescriptionClass).en ?: "Description unavailable", maxLines = 5, overflow = TextOverflow.Ellipsis, fontSize = 14.sp)
                }
            }
        }
    }
}