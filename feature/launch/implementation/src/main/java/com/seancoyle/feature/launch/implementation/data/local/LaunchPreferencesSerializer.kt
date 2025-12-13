package com.seancoyle.feature.launch.implementation.data.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.seancoyle.core.datastore_proto.LaunchPreferencesProto
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

internal class LaunchPreferencesSerializer @Inject constructor() : Serializer<LaunchPreferencesProto> {

    override val defaultValue: LaunchPreferencesProto = LaunchPreferencesProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): LaunchPreferencesProto =
        try {
            LaunchPreferencesProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: LaunchPreferencesProto, output: OutputStream) {
        t.writeTo(output)
    }
}
