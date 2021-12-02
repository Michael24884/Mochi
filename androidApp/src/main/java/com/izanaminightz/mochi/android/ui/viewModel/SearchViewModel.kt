package com.izanaminightz.mochi.android.ui.viewModel

import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.izanaminightz.mochi.data.MochiRepositoryImpl
import com.izanaminightz.mochi.domain.repository.MochiRepository
import com.izanaminightz.mochi.presentation.search.SearchModel
import com.izanaminightz.mochi.presentation.search.SearchUseCase
import com.izanaminightz.mochi.presentation.search.SearchUseCaseImpl
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class SearchViewModel : ViewModel() {

    private val searchUseCase: SearchUseCase = SearchUseCaseImpl(MochiRepositoryImpl())

    private val _results = MutableLiveData<SearchModel>()

    val results: LiveData<SearchModel>
        get() = _results

    init {
        observer()
    }

    private fun observer() {
        viewModelScope.launch {
            searchUseCase.observeSearchViewModel()
                .collect { _results.postValue(it) }
        }
    }

     suspend fun searchFor(query: String) {
        searchUseCase.compute(query)
    }

}