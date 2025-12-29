package com.seancoyle.core.common.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.cancellation.CancellationException

private const val DEFAULT_TIMEOUT = 5_000L

suspend fun <R> runSuspendCatching(block: suspend () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch(c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

fun <T> Flow<T>.stateIn(scope: CoroutineScope, initialValue: T): StateFlow<T> {
    return stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(DEFAULT_TIMEOUT),
        initialValue = initialValue,
    )
}
