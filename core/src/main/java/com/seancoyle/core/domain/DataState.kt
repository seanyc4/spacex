package com.seancoyle.core.domain


data class DataState<T>(
    val stateMessage: StateMessage? = null,
    val data: T? = null,
    val loading: Boolean = false,
) {

    companion object {

        fun <T> error(
            response: Response
        ): DataState<T> {
            return DataState(
                stateMessage = StateMessage(response),
                data = null
            )
        }

        fun <T> success(
            response: Response?,
            data: T? = null
        ): DataState<T> {
            return DataState(
                stateMessage = response?.let {
                    StateMessage(it)
                },
                data = data
            )
        }

        fun <T> loading(): DataState<T> = DataState(loading = true)
    }
}