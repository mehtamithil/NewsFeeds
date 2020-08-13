package com.newsfeeds.api

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    private val BASE_URL = "https://dl.dropboxusercontent.com/s/"
    private val HTTP_TIMEOUT = 30000L

    private val requestInterceptor = Interceptor { chain: Interceptor.Chain ->
        chain.proceed(chain.request().newBuilder().build())
    }

    val apiService: ApiService

    init {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(requestInterceptor)

        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
        }

        apiService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(okHttpClientBuilder.build())
            .build().create(ApiService::class.java)
    }
}