package com.simple.api.serialization

import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

interface Serializer {
    companion object {
        val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ").withZone(ZoneOffset.UTC)
    }
}