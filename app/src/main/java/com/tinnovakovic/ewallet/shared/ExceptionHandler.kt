package com.tinnovakovic.ewallet.shared

interface ExceptionHandler {

    fun getErrorMessage(throwable: Throwable): String
}
