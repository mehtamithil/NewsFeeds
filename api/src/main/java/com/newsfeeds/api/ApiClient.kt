package com.newsfeeds.api

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    /**
     * Singleton and Lazy instance building of ApiService
     */
    val apiService: ApiService by lazy {

        val baseUrl by lazy { "https://dl.dropboxusercontent.com/s/" }
        val timeout by lazy { 30000L }

        /**
         * For Api logging
         */
        val requestInterceptor by lazy {
            { chain: Interceptor.Chain -> chain.proceed(chain.request().newBuilder().build()) }
        }

        val okHttpClient by lazy {
            OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .addInterceptor(requestInterceptor)
                .also {
                    if (BuildConfig.DEBUG) {
                        it.addInterceptor(
                            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                        )
                    }
                }.build()
        }

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}