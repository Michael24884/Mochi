package com.izanaminightz.mochi.remote

import com.izanaminightz.mochi.domain.models.MangadexMangaFeed
import com.izanaminightz.mochi.domain.models.MangadexResultsModel
import com.izanaminightz.mochi.domain.models.MangadexResultsSingleModel
import io.ktor.client.*
import io.ktor.client.request.*


internal class MangadexApi(
    private val client: HttpClient
) {
    companion object {
        private const val baseURL = "https://api.mangadex.org"
        private const val parameters = "&contentRating[]=suggestive&includes[]=cover_art"
        private const val currentSeasonID = "a153b4e6-1fcc-4f45-a990-f37f989c0d74"
    }

    suspend fun fetchRecentlyAddedManga(): MangadexResultsModel = client.get("$baseURL/manga?order[updatedAt]=desc&limit=15$parameters")
    suspend fun fetchRandomManga() : MangadexResultsSingleModel = client.get("$baseURL/manga/random?includes[]=cover_art")
    suspend fun fetchSeasonalAnime() {
        val seasonList = client.get<String>("$baseURL/list/$currentSeasonID")
    }

    suspend fun fetchDetailManga(mangaID: String) : MangadexResultsSingleModel =
        client.get("$baseURL/manga/$mangaID?includes[]=cover_art")


    //TODO: ADD OFFSET For Current Manga
    suspend fun fetchDetailSingleMangaFeed(mangaID: String): MangadexMangaFeed =
        client.get("$baseURL/manga/$mangaID/feed?limit=1&order[chapter]=asc&translatedLanguage[]=en")

}



