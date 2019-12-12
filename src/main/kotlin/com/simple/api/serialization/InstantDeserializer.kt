package com.simple.api.serialization

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.simple.api.serialization.Serializer.Companion.fmt
import java.lang.reflect.Type
import java.time.Instant

class InstantDeserializer : JsonDeserializer<Instant>, Serializer {

    override fun deserialize(element: JsonElement?, type: Type?, context: JsonDeserializationContext?): Instant {
        return Instant.from(fmt.parse(element?.asString))
    }
}