package com.izanaminightz.mochi.domain.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


@Serializable
data class UserModel(
    val username: String,
    val id: String,
)

@Serializable
data class UserTokens(
    val session: String,
    val refresh: String,
    val expiresAt: LocalDateTime
)
