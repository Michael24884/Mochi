package com.izanaminightz.mochi.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class MangadexAtHome(
    val result: String,
    val baseUrl: String,
)
