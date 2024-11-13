package ru.evgeniykim.mychat.network.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.Interceptor
import okhttp3.Response

class MyIntercepter: Interceptor {
    private val token = ""
    private val job = CoroutineScope(Dispatchers.IO)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain
            .request()
            .newBuilder()
            .addHeader("accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build()

        return chain.proceed(request)
    }

//    private fun checkToken(): {
//        job.async {  }
//    }
}