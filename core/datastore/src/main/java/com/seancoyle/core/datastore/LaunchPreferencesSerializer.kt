package com.seancoyle.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class LaunchPreferencesSerializer @Inject constructor() : Serializer<LaunchPreferences> {
    override val defaultValue: LaunchPreferences = LaunchPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): LaunchPreferences =
        try {
            LaunchPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: LaunchPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}