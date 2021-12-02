package com.izanaminightz.mochi.presentation.reader

import kotlinx.serialization.Serializable


@Serializable
data class ReaderDataModel(
    val hash: String,
    val pages: List<String>,
    val chapterID: String,
    val chapter: String,
 ) : java.io.Serializable

sealed class ReaderModel {
    object TransitionPage: ReaderModel()
    data class State(val data: List<String>) : ReaderModel()
}