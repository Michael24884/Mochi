package com.izanaminightz.mochi.presentation.detail

import kotlinx.coroutines.flow.StateFlow

interface DetailUseCase {
    fun observerDetailModel() : StateFlow<DetailModel>
    suspend fun compute(mangaID: String)
    fun onDestroy()
}