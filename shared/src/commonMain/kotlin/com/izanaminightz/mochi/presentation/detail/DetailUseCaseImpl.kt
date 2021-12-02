package com.izanaminightz.mochi.presentation.detail

import com.izanaminightz.mochi.domain.repository.MochiRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine

class DetailUseCaseImpl(
    private val repository: MochiRepository,
    private val viewUpdate: ((DetailModel) -> Unit)? = null
) : DetailUseCase {

    private val couroutineScope: CoroutineScope = MainScope()
    private val detailModel = MutableStateFlow<DetailModel>(DetailModel.Loading)

    override fun observerDetailModel(): StateFlow<DetailModel> = detailModel

    override suspend fun compute(mangaID: String) {
        couroutineScope.launch { fetchDetail(mangaID) }
    }

     suspend fun fetchDetail(mangaID: String) {
         val detail = repository.fetchDetail(mangaID)
         val feed = repository.fetchFeed(mangaID)

         detail.combine(feed) { detail, feed ->
             DetailModel.State(
                 detail,
                 feed
             )
         }
             .collect {
                 detailModel.value = it
                 viewUpdate?.invoke(it)
             }
    }


    override fun onDestroy() {
        couroutineScope.cancel()
    }
}