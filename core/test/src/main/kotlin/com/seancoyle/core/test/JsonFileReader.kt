package com.seancoyle.core.test

import android.app.Application
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class JsonFileReader @Inject constructor(
    private val application: Application
) {
    fun readJSONFromAsset(fileName: String): String {
        val assetManager = application.assets
        val inputStream = assetManager.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }
}