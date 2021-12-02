package com.izanaminightz.mochi.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
enum class ResultModel {
    @SerialName("ok")
    OK,

    @SerialName("error")
    Error,
}

@Serializable
data class AuthModel(
    val result: ResultModel,
    val token: TokenModel? = null,
    val errors: List<ErrorModel>? = null,

)

@Serializable
data class TokenModel(
    val session: String,
    val refresh: String,
)


@Serializable
data class ErrorModel(
    val status: Int,
    val title: String,
    val detail: String,
)