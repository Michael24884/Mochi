package com.izanaminightz.mochi.presentation.feed

import kotlinx.coroutines.flow.StateFlow

interface FeedUseCase {
    fun observeFeedViewModel() : StateFlow<FeedModel>
    suspend fun compute( id: String)
    fun onDestroy()
}