package com.seancoyle.core.common.crashlytics

class FakeCrashlyticsImpl: CrashLogger {

    var exception: Throwable? = null
    val messages = mutableListOf<String>()

    override fun init(isEnabled: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setCustomKey(key: String, value: String) {
        TODO("Not yet implemented")
    }

    override fun logException(throwable: Throwable) {
        exception = throwable
    }

    override fun log(msg: String) {
        messages.add(msg)
    }
}
