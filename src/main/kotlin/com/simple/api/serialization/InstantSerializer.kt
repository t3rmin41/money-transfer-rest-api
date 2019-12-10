package com.simple.api.serialization

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.Instant
import java.time.ZoneOffset

import java.time.format.DateTimeFormatter

class InstantSerializer : JsonSerializer<Instant> {

    private val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ").withZone(ZoneOffset.UTC)

    override fun serialize(value: Instant?, type: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(fmt.format(value));
    }
}