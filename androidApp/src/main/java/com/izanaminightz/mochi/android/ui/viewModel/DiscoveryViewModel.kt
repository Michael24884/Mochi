package com.izanaminightz.mochi.android.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.izanaminightz.mochi.data.MochiRepositoryImpl
import com.izanaminightz.mochi.domain.repository.MochiRepository
import com.izanaminightz.mochi.presentation.discovery.DiscoveryModel
import com.izanaminightz.mochi.presentation.discovery.DiscoveryUseCase
import com.izanaminightz.mochi.presentation.discovery.DiscoveryUseCaseImpl
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DiscoveryViewModel : ViewModel() {

    private val discoveryUseCase: DiscoveryUseCase = DiscoveryUseCaseImpl(MochiRepositoryImpl())

    private val _data = MutableLiveData<DiscoveryModel>()
    val homeData: LiveData<DiscoveryModel>
        get() = _data

    init {
        observer()
        viewModelScope.launch {
            discoveryUseCase.computeDiscoveryDataSuspendable()
        }
    }


    private fun observer() {
        viewModelScope.launch {
            discoveryUseCase.observerDiscoveryModel().collect {
                _data.postValue(it)
            }
        }
    }


}