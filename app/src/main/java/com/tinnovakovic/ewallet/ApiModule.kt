package com.tinnovakovic.ewallet

import com.google.gson.Gson
import com.tinnovakovic.ewallet.data.EtherscanApi
import com.tinnovakovic.ewallet.data.EthplorerApi
import com.tinnovakovic.ewallet.shared.ApiType
import com.tinnovakovic.ewallet.shared.ResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun providesEthplorerApi(okHttpClient: OkHttpClient.Builder, gsonConverterFactory: GsonConverterFactory): EthplorerApi {
        return Retrofit.Builder()
            .baseUrl("https://api.ethplorer.io/")
            .client(
                okHttpClient
                    .build()
            )
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(ResultCallAdapterFactory(ApiType.Generic))
            .build()
            .create(EthplorerApi::class.java)
    }

    @Provides
    @Singleton
    fun providesEtherscanApi(okHttpClient: OkHttpClient.Builder, gsonConverterFactory: GsonConverterFactory): EtherscanApi {
        return Retrofit.Builder()
            .baseUrl("https://api.etherscan.io/")
            .client(
                okHttpClient
                    .build()
            )
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(ResultCallAdapterFactory(ApiType.Etherscan))
            .build()
            .create(EtherscanApi::class.java)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }

    @Provides
    @Singleton
    fun providesGsonConverter(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}