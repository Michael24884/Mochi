package com.izanaminightz.mochi.presentation.mylist

import com.izanaminightz.mochi.domain.models.UserListModel
import kotlinx.coroutines.flow.StateFlow

interface ListUseCase {
    fun observe() : StateFlow<ListModel>
    suspend fun compute()
    fun onDestroy()
}