package com.izanaminightz.mochi.presentation.mylist

import com.izanaminightz.mochi.domain.models.UserListModel

sealed class ListModel {
    object Refreshing : ListModel()
    data class Error(val message: String) : ListModel()
   data class State(val list: UserListModel) : ListModel()
}
