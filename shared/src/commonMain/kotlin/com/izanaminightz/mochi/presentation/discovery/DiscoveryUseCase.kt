package com.izanaminightz.mochi.presentation.discovery

import kotlinx.coroutines.flow.StateFlow

interface DiscoveryUseCase {
    fun observerDiscoveryModel(): StateFlow<DiscoveryModel>
    fun computeData()
    suspend fun computeDiscoveryDataSuspendable()
}