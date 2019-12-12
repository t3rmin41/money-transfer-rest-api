package com.simple.api.http

import com.google.gson.JsonElement
import com.simple.api.errors.ErrorMessage

data class StandardResponse(
    val status: StatusResponse,
    val data: JsonElement?,
    val message: String?,
    val errors: List<ErrorMessage>? = null
)