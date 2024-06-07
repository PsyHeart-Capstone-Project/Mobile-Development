package com.capstone.psyheart.api

import com.capstone.psyheart.App.Companion.getAppContext
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
// import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor;

object ApiConfig {
    fun getApiService(): ApiService {
        /*val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(
                if (com.capstone.psyheart.BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE
            )*/

        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(
                    HttpLoggingInterceptor.Level.BODY
            )

       /* val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor( OkHttpProfilerInterceptor() )
        }
        val client = builder.build()
        val retrofit = Retrofit.Builder()
        ......
        .client(client)
            .build()*/

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
             .addInterceptor( OkHttpProfilerInterceptor() )
            .build()

        /*// if (BuildConfig.DEBUG) {
        val apiLogInterceptor = ChuckerInterceptor.Builder(getAppContext()).build()
        clientBuilder.addInterceptor(apiLogInterceptor)
        // }

        val client = clientBuilder.build()*/

        val retrofit = Retrofit.Builder()
            .baseUrl(com.capstone.psyheart.BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}