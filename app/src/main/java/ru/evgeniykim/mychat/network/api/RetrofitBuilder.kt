package ru.evgeniykim.mychat.network.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitBuilder {
    private const val BASE_URL = "https://plannerok.ru/"

    private val httpClient = OkHttpClient()
    private val loggingIntercepter = HttpLoggingInterceptor { message -> println("API logs $message") }

    private fun getRetrofit(): Retrofit {

        loggingIntercepter.setLevel(HttpLoggingInterceptor.Level.BASIC)

        httpClient
            .newBuilder()
            .addInterceptor(loggingIntercepter)
            .addInterceptor(MyIntercepter())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val retrofitApiService: MyApi = getRetrofit().create(MyApi::class.java)
}