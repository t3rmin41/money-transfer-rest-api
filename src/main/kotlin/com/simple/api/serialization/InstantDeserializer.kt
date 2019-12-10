package com.simple.api.serialization

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.Instant

import java.time.ZoneOffset

import java.time.format.DateTimeFormatter

class InstantDeserializer : JsonDeserializer<Instant> {

    private val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ").withZone(ZoneOffset.UTC)

    override fun deserialize(element: JsonElement?, type: Type?, context: JsonDeserializationContext?): Instant {
        return Instant.from(fmt.parse(element?.asString))
    }
}