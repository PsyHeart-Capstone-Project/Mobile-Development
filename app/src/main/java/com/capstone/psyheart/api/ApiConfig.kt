package com.capstone.psyheart.api

import com.capstone.psyheart.App.Companion.getAppContext
import com.chuckerteam.chucker.BuildConfig
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun getApiService(): ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(
                if (com.capstone.psyheart.BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE
            )

        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .build()
            chain.proceed(requestHeaders)
        }

        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)

        // if (BuildConfig.DEBUG) {
            val apiLogInterceptor = ChuckerInterceptor.Builder(getAppContext()).build()
            clientBuilder.addInterceptor(apiLogInterceptor)
        // }

        val client = clientBuilder.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(com.capstone.psyheart.BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}