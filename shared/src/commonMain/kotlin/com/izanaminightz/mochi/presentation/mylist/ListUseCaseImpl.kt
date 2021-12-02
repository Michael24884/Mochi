package com.izanaminightz.mochi.presentation.mylist

import com.izanaminightz.mochi.data.MochiRepositoryImpl
import com.izanaminightz.mochi.domain.models.UserListModel
import com.izanaminightz.mochi.domain.repository.MochiRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ListUseCaseImpl(
    private val repository: MochiRepository,
    private val viewUpdate: ((ListModel) -> Unit)? = null,
) : ListUseCase {
    private val coroutineScope = MainScope()

    private val _data = MutableStateFlow<ListModel>(ListModel.Refreshing)


    override fun observe(): StateFlow<ListModel> =
        _data

    override suspend fun compute() {
        coroutineScope.launch { fetchList() }
    }

    private suspend fun fetchList() {
        repository.fetchUserStatusList()
            .collect {
                val state = ListModel.State(it)
                _data.value = state
                viewUpdate?.invoke(state)
            }
    }


    override fun onDestroy() {
        coroutineScope.cancel()
    }


}