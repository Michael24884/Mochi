package com.izanaminightz.mochi.domain.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class UserListModel(
 val result: String,
 val statuses: JsonObject
)

