package com.izanaminightz.mochi.android.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.izanaminightz.mochi.data.MochiRepositoryImpl
import com.izanaminightz.mochi.presentation.feed.FeedModel
import com.izanaminightz.mochi.presentation.feed.FeedUseCase
import com.izanaminightz.mochi.presentation.feed.FeedUseCaseImpl
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.compose.viewModel

class FeedViewModel: ViewModel() {
    private val feedUseCase : FeedUseCase = FeedUseCaseImpl(MochiRepositoryImpl())

    private val _feed = MutableLiveData<FeedModel>()
    val feed: LiveData<FeedModel>
        get() = _feed


    init {
           observer()
    }

    fun fetchFeed(id: String) {
        viewModelScope.launch {
            feedUseCase.compute(id)
        }
    }

    private fun observer() {
        viewModelScope.launch {
            feedUseCase.observeFeedViewModel()
                .collect { _feed.postValue(it) }
        }
    }


}