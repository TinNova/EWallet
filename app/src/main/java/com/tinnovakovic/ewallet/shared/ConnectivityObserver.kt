package com.tinnovakovic.ewallet.shared

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observerIsOnline() : Flow<Boolean>
}