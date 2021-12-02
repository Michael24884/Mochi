package com.izanaminightz.mochi.android.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.izanaminightz.mochi.data.MochiRepositoryImpl
import com.izanaminightz.mochi.domain.models.MangadexFeedData
import com.izanaminightz.mochi.domain.models.MangadexMangaFeed
import com.izanaminightz.mochi.presentation.detail.DetailModel
import com.izanaminightz.mochi.presentation.detail.DetailUseCase
import com.izanaminightz.mochi.presentation.detail.DetailUseCaseImpl
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailViewModel: ViewModel() {

    private val detailUseCase : DetailUseCase = DetailUseCaseImpl(MochiRepositoryImpl())

    private val _data = MutableLiveData<DetailModel>()
    private val _feedData = MutableLiveData<MangadexMangaFeed>()


    val manga: LiveData<DetailModel>
        get() = _data

    val feed: LiveData<MangadexMangaFeed>
        get() = _feedData

    init {
        observer()
    }

     private fun observer() {
        viewModelScope.launch {
            detailUseCase.observerDetailModel()
                .collect { _data.postValue(it) }
        }
    }


    suspend fun fetchManga(id: String) {
        detailUseCase.compute(id)

    }

}