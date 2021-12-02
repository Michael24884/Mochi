package com.izanaminightz.mochi.presentation.discovery

import com.izanaminightz.mochi.domain.models.Datum

sealed class DiscoveryModel {
    object LoadingDiscoveryState : DiscoveryModel()
    data class DiscoveryState(
        val recentlyAddedManga : List<Datum>?
    ) : DiscoveryModel()
    data class Error(val error: String) : DiscoveryModel()



    override fun toString(): String {
        return when(this) {
            is LoadingDiscoveryState -> "Loading Discovery"
            is DiscoveryState -> "Discovery State"
            is Error -> "Error!!"
        }
    }
}