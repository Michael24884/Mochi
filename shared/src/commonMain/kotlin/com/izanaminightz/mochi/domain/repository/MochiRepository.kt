package com.izanaminightz.mochi.domain.repository

import com.izanaminightz.mochi.domain.models.*
import kotlinx.coroutines.flow.Flow

interface MochiRepository {
    suspend fun fetchMangadexRecentList() : Flow<MangadexResultsModel>
    suspend fun fetchDetail(id: String) : Flow<MangadexResultsSingleModel>
    suspend fun fetchFeed(id: String) : Flow<MangadexMangaFeed>
    suspend fun searchResultsFor(query: String) : Flow<MangadexResultsModel>
    suspend fun fetchMangaChapterHomeURL(chapterID: String) : Flow<String>
    suspend fun fetchUserStatusList() : Flow<UserListModel>

    //Login
    suspend fun login(userPreferred: String, password: String) : AuthModel
    suspend fun fetchUser(token: String) : UserModel
}