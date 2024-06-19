package com.capstone.psyheart.api

import com.capstone.psyheart.App.Companion.getAppContext
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun getApiService(): ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY
            )

        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .build()
            chain.proceed(requestHeaders)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(ChuckerInterceptor(getAppContext()))
            .addInterceptor(OkHttpProfilerInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(com.capstone.psyheart.BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}