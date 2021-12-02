package com.izanaminightz.mochi.presentation.discovery

import com.izanaminightz.mochi.domain.models.MangadexResultsModel
import com.izanaminightz.mochi.domain.repository.MochiRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import android.util.*

class DiscoveryUseCaseImpl(
    private val repository: MochiRepository,
    private val viewUpdate: ((DiscoveryModel) -> Unit)? = null
) : DiscoveryUseCase {

    private val coroutineScope: CoroutineScope = MainScope()

    private val discoveryModel = MutableStateFlow<DiscoveryModel>(DiscoveryModel.LoadingDiscoveryState)

    override fun observerDiscoveryModel(): StateFlow<DiscoveryModel> = discoveryModel

    override fun computeData() {
         coroutineScope.launch {
            computeDiscoveryDataSuspendable()
        }
    }


    override suspend fun computeDiscoveryDataSuspendable() {
         repository.fetchMangadexRecentList()
             .catch { cause: Throwable ->
                 val error = DiscoveryModel.Error("Something wrong!")
                 discoveryModel.value = error
                 Log.d("error", error.error)
                 viewUpdate?.invoke(error)
             }
            .collect { data ->
                val state = DiscoveryModel.DiscoveryState(data.data)
                discoveryModel.value = state
                viewUpdate?.invoke(state)
            }
    }


    fun onDestroy() = coroutineScope.cancel()

}