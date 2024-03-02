package com.tinnovakovic.ewallet.shared.network

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observerIsOnline() : Flow<Boolean>
}