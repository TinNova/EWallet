package com.tinnovakovic.ewallet.domain

import retrofit2.HttpException
import javax.inject.Inject

class ErrorHandler @Inject constructor() {

    fun execute(throwable: Throwable): Throwable {
       return when (throwable) {
            is HttpException -> throwable
            else -> throwable
        }
    }
}