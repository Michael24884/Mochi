package com.izanaminightz.mochi.android.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.izanaminightz.mochi.domain.models.repository.DetailRepository
import com.izanaminightz.mochi.domain.models.repository.MangadexHomeRepository
import kotlinx.coroutines.launch

class MangadexDetailViewModel(
    private val repository: DetailRepository
) : ViewModel() {


    val detailManga = repository.manga
    val detailMangaFeed = repository.feed



    fun fetchDetailManga(id: String) {
        viewModelScope.launch {
            repository.fetchDetailMangaWithFeed(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.clear()
    }
}