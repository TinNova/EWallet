package com.tinnovakovic.ewallet.shared

import android.app.Application

interface ContextProvider {

    fun getContext(): Application
}