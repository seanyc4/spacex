package com.seancoyle.core.state


data class DataState<T>(
    var stateMessage: StateMessage? = null,
    var data: T? = null,
    var event: Event? = null
) {

    companion object {

        fun <T> error(
            response: Response,
            event: Event?
        ): DataState<T> {
            return DataState(
                stateMessage = StateMessage(response),
                data = null,
                event = event
            )
        }

        fun <T> data(
            response: Response?,
            data: T? = null,
            event: Event?
        ): DataState<T> {
            return DataState(
                stateMessage = response?.let {
                    StateMessage(it)
                },
                data = data,
                event = event
            )
        }
    }
}