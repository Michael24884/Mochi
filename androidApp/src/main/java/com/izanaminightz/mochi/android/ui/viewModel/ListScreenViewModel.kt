package com.izanaminightz.mochi.android.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.izanaminightz.mochi.android.MainApplication
import com.izanaminightz.mochi.data.MochiRepositoryImpl
import com.izanaminightz.mochi.presentation.mylist.ListModel
import com.izanaminightz.mochi.presentation.mylist.ListUseCase
import com.izanaminightz.mochi.presentation.mylist.ListUseCaseImpl
import com.izanaminightz.mochi.utils.MochiHelper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ListScreenViewModel : ViewModel() {

    private val useCase: ListUseCase = ListUseCaseImpl(MochiRepositoryImpl(MochiHelper().getTokens()))

    private val _list = MutableLiveData<ListModel>()

    val list
        get() = _list

    init {
        observer()
        fetchList()
    }

    private fun fetchList() {
        viewModelScope.launch {
            useCase.compute()
        }
    }

    private fun observer() {
        viewModelScope.launch {
            useCase.observe()
                .collect { _list.postValue(it) }
        }
    }

}