package com.izanaminightz.mochi.android.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.izanaminightz.mochi.domain.models.Datum
import com.izanaminightz.mochi.domain.models.RelationshipType
import com.izanaminightz.mochi.utils.MangadexHelper

@Composable
fun ListCard(
    datum: Datum,
    onSelected: () -> Unit,
) {
    val image = datum.relationships?.first { it.type == RelationshipType.Cover_Art }?.attributes?.fileName
    Card(
        elevation = 6.dp,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .fillMaxWidth()
            .padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
            .clickable { onSelected() }
    ) {
       Row(Modifier.padding(all = 2.dp)) {
           Image(
               rememberImagePainter(MangadexHelper().coverCreator(datum.id!!, image)),
               contentDescription = "cover_art",
               contentScale = ContentScale.FillBounds,
           modifier = Modifier.clip(RoundedCornerShape(4.dp))
               .size(115.dp, 150.dp)
               .padding(end = 8.dp)
               )
           Text(datum.attributes?.title?.en ?: datum.attributes?.title?.ja ?: "???", fontSize = 17.sp, fontWeight = FontWeight.Bold)
       }
    }
}
