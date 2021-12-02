package com.izanaminightz.mochi.presentation.search

import kotlinx.coroutines.flow.StateFlow

interface SearchUseCase {
    fun observeSearchViewModel() : StateFlow<SearchModel>
    suspend fun compute(query: String)
    fun onDestroy()
}