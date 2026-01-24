package com.seancoyle.feature.launch.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException

internal class RetryInterceptor(
    private val maxRetries: Int = 3
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var lastException: IOException? = null

        repeat(maxRetries) { attempt ->
            try {
                val response = chain.proceed(request)

                // If successful or client error (4xx), return immediately
                if (response.isSuccessful || response.code in 400..499) {
                    return response
                }

                // For server errors (5xx), close the response and retry
                if (response.code in 500..599 && attempt < maxRetries - 1) {
                    Timber.w("Server error ${response.code}, retrying... (attempt ${attempt + 1}/$maxRetries)")
                    response.close()
                    attempt + 1
                } else {
                    return response
                }
            } catch (e: SocketTimeoutException) {
                lastException = e
                Timber.w(e, "SocketTimeoutException, retrying... (attempt ${attempt + 1}/$maxRetries)")
                if (attempt < maxRetries - 1) {
                    attempt + 1
                }
            } catch (e: IOException) {
                lastException = e
                Timber.w(e, "IOException, retrying... (attempt ${attempt + 1}/$maxRetries)")
                if (attempt < maxRetries - 1) {
                    attempt + 1
                }
            }
        }

        throw lastException ?: IOException("Request failed after $maxRetries attempts")
    }
}
