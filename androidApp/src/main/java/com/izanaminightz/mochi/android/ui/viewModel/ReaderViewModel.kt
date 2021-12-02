package com.izanaminightz.mochi.android.ui.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.izanaminightz.mochi.android.MainApplication
import com.izanaminightz.mochi.data.MochiRepositoryImpl
import com.izanaminightz.mochi.domain.repository.MochiRepository
import com.izanaminightz.mochi.presentation.reader.ReaderDataModel
import com.izanaminightz.mochi.presentation.reader.ReaderModel
import com.izanaminightz.mochi.presentation.reader.ReaderUseCase
import com.izanaminightz.mochi.presentation.reader.ReaderUseCaseImpl
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication

class ReaderViewModel: ViewModel() {

    private val readerUseCase: ReaderUseCase = ReaderUseCaseImpl(MochiRepositoryImpl())

    private val imageLoader = ImageLoader.Builder(MainApplication.applicationContext())
        .availableMemoryPercentage(0.25)
        .crossfade(true)
        .build()

    private val _data = MutableLiveData<ReaderModel>()
    val data: LiveData<ReaderModel>
        get() = _data



    init {
        observer()
    }


      fun fetchChapters(model: ReaderDataModel) {
        readerUseCase.compute(model)
    }

    fun dispose() {
        imageLoader.shutdown()
    }

    private fun preloadImages(images: List<String> ) {
        for (image in images) {
            val request = ImageRequest.Builder(MainApplication.applicationContext())
                .data(image)
                .memoryCachePolicy(CachePolicy.DISABLED)
                .build()

            Log.w("Image request", "Requesting image for $image")
            imageLoader.enqueue(request)
        }

    }


    private fun observer() {
        viewModelScope.launch {
            readerUseCase.observeReaderModel().collect {
                if (it is ReaderModel.State) {
                    preloadImages(it.data)
                }
                _data.postValue(it)
            }
        }
    }

}