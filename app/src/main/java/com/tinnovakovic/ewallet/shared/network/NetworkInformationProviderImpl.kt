package com.tinnovakovic.ewallet.shared.network

import com.tinnovakovic.ewallet.data.NetworkInMemoryCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

class NetworkInformationProviderImpl @Inject constructor(
    private val connectivityObserver: ConnectivityObserver,
    @Singleton val applicationCoroutineScope: CoroutineScope,
    private val networkInMemoryCache: NetworkInMemoryCache
) : NetworkInformationProvider {

    override fun observeNetwork() {
        connectivityObserver
            .observerIsOnline()
            .catch { e -> e.printStackTrace() }
            .onEach {
                networkInMemoryCache.updateCache(it)
            }
            .launchIn(applicationCoroutineScope)
    }
}
