package com.izanaminightz.mochi.presentation.feed

import com.izanaminightz.mochi.domain.models.MangadexMangaFeed

sealed class FeedModel {
    object Loading : FeedModel()
    data class Error(val message: String) : FeedModel()
    data class State(val feed: MangadexMangaFeed) : FeedModel()
}