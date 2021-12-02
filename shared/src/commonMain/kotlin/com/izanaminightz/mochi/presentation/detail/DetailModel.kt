package com.izanaminightz.mochi.presentation.detail

import com.izanaminightz.mochi.domain.models.MangadexMangaFeed
import com.izanaminightz.mochi.domain.models.MangadexResultsSingleModel

sealed class DetailModel {
    object Loading : DetailModel()
    object FeedLoading: DetailModel()
    data class Error(val message: String) : DetailModel()
    data class State(
        val detail: MangadexResultsSingleModel,
        val feed: MangadexMangaFeed? = null,
    ) : DetailModel()


    override fun toString(): String {
        return when(this) {
            is Loading -> "Loading Detail"
            is Error -> "Error!"
            is State -> "Detail State"
            is FeedLoading -> "Feed Loading"
        }
    }

}