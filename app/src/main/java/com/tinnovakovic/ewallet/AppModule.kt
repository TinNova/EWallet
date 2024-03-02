package com.tinnovakovic.ewallet

import android.app.Application
import com.tinnovakovic.ewallet.data.NetworkInMemoryCache
import com.tinnovakovic.ewallet.shared.ApplicationCoroutineScope
import com.tinnovakovic.ewallet.shared.ConnectivityObserver
import com.tinnovakovic.ewallet.shared.ConnectivityObserverImpl
import com.tinnovakovic.ewallet.shared.ContextProvider
import com.tinnovakovic.ewallet.shared.ContextProviderImpl
import com.tinnovakovic.ewallet.shared.ExceptionHandler
import com.tinnovakovic.ewallet.shared.ExceptionHandlerImpl
import com.tinnovakovic.ewallet.shared.NetworkInformationProvider
import com.tinnovakovic.ewallet.shared.NetworkInformationProviderImpl
import com.tinnovakovic.ewallet.shared.RateLimitHandler
import com.tinnovakovic.ewallet.shared.RateLimitHandlerImpl
import com.tinnovakovic.ewallet.shared.RetryIo
import com.tinnovakovic.ewallet.shared.RetryIoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesContextProvider(application: Application): ContextProvider =
        ContextProviderImpl(application)

    @Provides
    @Singleton
    fun providesRetryIo(): RetryIo = RetryIoImpl()

    @Provides
    @Singleton
    fun providesRateLimitHandler(): RateLimitHandler = RateLimitHandlerImpl()

    @Provides
    @Singleton
    fun providesExceptionHandler(contextProvider: ContextProvider): ExceptionHandler = ExceptionHandlerImpl(contextProvider)

    @Provides
    @Singleton
    fun providesConnectivityObserver(contextProvider: ContextProvider): ConnectivityObserver =
        ConnectivityObserverImpl(contextProvider)

    @Provides
    @Singleton
    fun providesNetworkInformationProvider(
        connectivityObserver: ConnectivityObserver,
        applicationCoroutineScope: CoroutineScope,
        networkInMemoryCache: NetworkInMemoryCache
    ): NetworkInformationProvider =
        NetworkInformationProviderImpl(connectivityObserver, applicationCoroutineScope, networkInMemoryCache)

    @Provides
    @Singleton
    fun provideApplicationScope(applicationCoroutineScope: ApplicationCoroutineScope): CoroutineScope {
        return applicationCoroutineScope.coroutineScope
    }
}
