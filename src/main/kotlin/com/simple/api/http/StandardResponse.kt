package com.simple.api.http

import com.google.gson.JsonElement

data class StandardResponse(
        val status: StatusResponse,
        val data: JsonElement?,
        val message: String?
)