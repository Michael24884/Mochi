package com.izanaminightz.mochi.android.model

import kotlinx.serialization.Serializable


@Serializable
data class MangaChapters(
    val chapter: String,
    val chapters: List<String>
)
