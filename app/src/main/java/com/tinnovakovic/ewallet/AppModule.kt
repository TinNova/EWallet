package com.tinnovakovic.ewallet

import android.app.Application
import com.tinnovakovic.ewallet.shared.ContextProvider
import com.tinnovakovic.ewallet.shared.ContextProviderImpl
import com.tinnovakovic.ewallet.shared.RateLimitHandler
import com.tinnovakovic.ewallet.shared.RateLimitHandlerImpl
import com.tinnovakovic.ewallet.shared.RetryIo
import com.tinnovakovic.ewallet.shared.RetryIoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesContextProvider(application: Application): ContextProvider = ContextProviderImpl(application)

    @Provides
    @Singleton
    fun providesRetryIo(): RetryIo = RetryIoImpl()

    @Provides
    @Singleton
    fun providesRateLimitHandler(): RateLimitHandler = RateLimitHandlerImpl()
}