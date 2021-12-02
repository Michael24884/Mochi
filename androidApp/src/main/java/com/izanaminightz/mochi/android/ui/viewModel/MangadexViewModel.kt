package com.izanaminightz.mochi.android.ui.viewModel

import androidx.lifecycle.*
import com.izanaminightz.mochi.domain.models.repository.MangadexHomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MangadexHomeViewModel(
    private val repository: MangadexHomeRepository
) : ViewModel() {

    val recentlyAdded = repository.recents
    val randomMangaList = repository.randomMangaList


    init {
        viewModelScope.launch {
            if (recentlyAdded.value == null)
                repository.beginFetch()
        }
    }



}