package com.izanaminightz.mochi.presentation.reader

import com.izanaminightz.mochi.domain.repository.MochiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ReaderUseCaseImpl(
    private val repository: MochiRepository,
    private val viewUpdate: ((ReaderModel) -> Unit)? = null,
) : ReaderUseCase {

    private val coroutineScope: CoroutineScope = MainScope()
    private val _model = MutableStateFlow<ReaderModel>(ReaderModel.TransitionPage)



    override fun observeReaderModel(): StateFlow<ReaderModel> = _model

    override fun compute(model: ReaderDataModel) {
        coroutineScope.launch { computeReaderSuspendable(model) }
    }

    override suspend fun computeReaderSuspendable(model: ReaderDataModel) {
        repository.fetchMangaChapterHomeURL(model.chapterID)
            .collect { url ->
                val lists = model.pages.map { "${url}/data/${model.hash}/$it" }
                val state = ReaderModel.State(lists)
                _model.value = state
                viewUpdate?.invoke(state)
            }
    }



    override fun onDestroy() {
        coroutineScope.cancel()
    }


}