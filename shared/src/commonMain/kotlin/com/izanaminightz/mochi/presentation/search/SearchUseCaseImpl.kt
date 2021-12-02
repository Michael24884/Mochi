package com.izanaminightz.mochi.presentation.search

import com.izanaminightz.mochi.domain.repository.MochiRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchUseCaseImpl(
    private val repository: MochiRepository,
    private val viewUpdate: ((SearchModel) -> Unit)? = null,
) : SearchUseCase {

    private val coroutineScope = MainScope()

    private val _searchData = MutableStateFlow<SearchModel>(SearchModel.Idle)

    override fun observeSearchViewModel(): StateFlow<SearchModel> = _searchData

    override suspend fun compute(query: String) {
        coroutineScope.launch { fetchSearchResults(query) }
    }

    private suspend fun fetchSearchResults(query: String)  {
        _searchData.value = SearchModel.Loading
        repository.searchResultsFor(query)
            .catch {
                _searchData.value = SearchModel.Error(it.message ?: "An error has occurred in Search UseCase")
            }
            .collect {
                val state = SearchModel.State(it)
                _searchData.value = state
                viewUpdate?.invoke(state)
            }

    }

    override fun onDestroy() {
        coroutineScope.cancel()
    }


}