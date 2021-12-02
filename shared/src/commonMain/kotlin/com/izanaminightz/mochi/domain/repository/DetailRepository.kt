package com.izanaminightz.mochi.domain.models.repository

import com.izanaminightz.mochi.domain.models.MangadexFeedData
import com.izanaminightz.mochi.domain.models.MangadexMangaFeed
import com.izanaminightz.mochi.domain.models.MangadexResultsSingleModel
import com.izanaminightz.mochi.remote.MangadexApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DetailRepository: KoinComponent {
    private val mangadexApi: MangadexApi by inject()

    private val _manga = MutableStateFlow<MangadexResultsSingleModel?>(null)
    private val _feed = MutableStateFlow<MangadexMangaFeed?>(null)

    val manga: StateFlow<MangadexResultsSingleModel?> = _manga
    val feed: StateFlow<MangadexMangaFeed?> = _feed


    fun clear() {
        _manga.value = null
        _feed.value = null
    }


    suspend fun fetchDetailMangaWithFeed(id: String) {
        _manga.value = mangadexApi.fetchDetailManga(id)
        _feed.value = mangadexApi.fetchDetailSingleMangaFeed(id)
    }




}