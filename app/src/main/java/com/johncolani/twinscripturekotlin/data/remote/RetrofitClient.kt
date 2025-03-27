package com.johncolani.twinscripturekotlin.data.remote

import android.util.Log
import com.johncolani.twinscripturekotlin.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d("RetrofitClient", message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("api-key", Constants.API_KEY)
                .build()
            chain.proceed(request)
        }
        .build()

    val retrofit: Retrofit

    init {
        Log.d("RetrofitClient", "Initializing Retrofit with baseUrl: ${Constants.BASE_URL}")
        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}