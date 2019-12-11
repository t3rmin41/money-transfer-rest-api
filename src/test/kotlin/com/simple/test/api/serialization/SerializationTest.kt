package com.simple.test.api.serialization

import com.google.gson.Gson
import com.simple.api.serialization.InstantDeserializer
import com.simple.api.serialization.InstantSerializer
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.test.assertEquals

class SerializationTest {
    private val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ").withZone(ZoneOffset.UTC)

    val instantSerializer = InstantSerializer()
    val instantDeserializer = InstantDeserializer()

    val now = Instant.now()

    val datetimeString = "2019-12-11 00:00:00+0000"
    val instant = Instant.ofEpochMilli(1576022400000)

    //@Disabled
    @Test
    fun testInstantSerialization() {
        assertEquals("\""+fmt.format(now)+"\"", instantSerializer.serialize(value = now, type = null, context = null).toString());
    }

    @Test
    fun testInstantDeserialization() {
        assertEquals(instant, instantDeserializer.deserialize(Gson().toJsonTree(datetimeString), null, null));
    }
}