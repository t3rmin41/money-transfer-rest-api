package com.simple.api.serialization

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.simple.api.serialization.Serializer.Companion.fmt
import java.lang.reflect.Type
import java.time.Instant
import java.time.ZoneOffset

import java.time.format.DateTimeFormatter

class InstantSerializer : JsonSerializer<Instant>, Serializer {

    override fun serialize(value: Instant?, type: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(fmt.format(value));
    }
}