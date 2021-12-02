package com.izanaminightz.mochi.presentation.feed

import com.izanaminightz.mochi.domain.repository.MochiRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FeedUseCaseImpl(
    private val repository: MochiRepository,
    private val viewUpdate: ((FeedModel) -> Unit)? = null,
) : FeedUseCase {

    private val coroutineScope = MainScope()

    private val _feedData = MutableStateFlow<FeedModel>(FeedModel.Loading)

    override fun observeFeedViewModel(): StateFlow<FeedModel> = _feedData

    override suspend fun compute(id: String) {
        coroutineScope.launch { fetchFeed(id) }
    }


    private suspend fun fetchFeed(id: String) {
        repository.fetchFeed(id)
            .collect {
                val state = FeedModel.State(it)
                _feedData.value = state
                viewUpdate?.invoke(state)
            }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
    }


}