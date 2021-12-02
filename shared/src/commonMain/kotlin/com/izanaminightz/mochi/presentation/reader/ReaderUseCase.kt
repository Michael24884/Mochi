package com.izanaminightz.mochi.presentation.reader

import kotlinx.coroutines.flow.StateFlow

interface ReaderUseCase {
    fun observeReaderModel() : StateFlow<ReaderModel>
    fun compute(model: ReaderDataModel)
    suspend fun computeReaderSuspendable(model: ReaderDataModel)
    fun onDestroy()
}