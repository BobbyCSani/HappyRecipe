package com.happy.recipe.network.remote

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor(): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .addHeader(
                "x-api-key",
                "7d1e95cf5f4f4306a1ded268d848074a"
            )
            .build()
        return chain.proceed(authenticatedRequest)
    }
}