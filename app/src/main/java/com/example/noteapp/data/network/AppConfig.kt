package com.example.noteapp.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by tuong.nguyen2 on 09/09/2022.
 */
object AppConfig {
    // change url here when rerunning server
    private const val BASE_URL = "http://172.18.29.41:8080"

    internal fun createService(): Retrofit {
        // add log -> log http status & response in logcat verbose
        val httpClientBuilder = OkHttpClient.Builder()
        HttpLoggingInterceptor().let {
            it.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addInterceptor(it)
        }

        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())

        return builder.build()
    }
}
