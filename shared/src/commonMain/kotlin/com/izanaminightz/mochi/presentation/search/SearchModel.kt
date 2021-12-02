package com.izanaminightz.mochi.presentation.search

import com.izanaminightz.mochi.domain.models.MangadexResultsModel

sealed class SearchModel {
    object Loading: SearchModel()
    object Idle: SearchModel()
    data class State(val results: MangadexResultsModel) : SearchModel()
    data class Error(val message: String) : SearchModel()
}