package com.izanaminightz.mochi.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MangadexMangaFeed(
    val data: List<MangadexFeedData>,
    val result: String,
)

@Serializable
data class MangadexFeedData(
    val id: String,
    val type: String,
    val attributes: MangaFeedAttributes,

    val relationships: List<Relationship>
)

@Serializable
data class MangaFeedAttributes(
    val volume: String? = null,
    val chapter: String? = null,
    val title: String? = "N/A",
    val translatedLanguage: String,
    val hash: String,
    @SerialName("data") val pages: List<String>,
    @SerialName("dataSaver") val pagesSaver: List<String>,
    val createdAt: String,
)
