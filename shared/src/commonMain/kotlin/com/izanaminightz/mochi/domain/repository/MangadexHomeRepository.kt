package com.izanaminightz.mochi.domain.models.repository

import com.izanaminightz.mochi.domain.models.MangadexFeedData
import com.izanaminightz.mochi.domain.models.MangadexMangaFeed
import com.izanaminightz.mochi.domain.models.MangadexResultsModel
import com.izanaminightz.mochi.domain.models.MangadexResultsSingleModel
import com.izanaminightz.mochi.remote.MangadexApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MangadexHomeRepository: KoinComponent {
    private val mangadexApi: MangadexApi by inject()

    private val _recents = MutableStateFlow<MangadexResultsModel?>(null)
    private val _randomManga = MutableStateFlow<List<MangadexResultsSingleModel>>(listOf())




    val recents: StateFlow<MangadexResultsModel?> = _recents
    val randomMangaList: StateFlow<List<MangadexResultsSingleModel>> = _randomManga


    suspend fun beginFetch() {
        fetchMangadexRecentList()
        fetchRandomManga()
    }

    private suspend fun fetchMangadexRecentList() {
      _recents.value = mangadexApi.fetchRecentlyAddedManga()
    }

    private suspend fun fetchRandomManga() {
        _randomManga.value += mangadexApi.fetchRandomManga()
    }



}