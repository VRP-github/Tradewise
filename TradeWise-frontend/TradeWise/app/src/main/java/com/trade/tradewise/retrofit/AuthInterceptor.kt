package com.star.starmedicahub.retrofit

import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestUrl = originalRequest.url.toString()

        // Skip adding Authorization header for public endpoints
        if (requestUrl.contains("register", ignoreCase = true) ||
            requestUrl.contains("login", ignoreCase = true)) {
            return chain.proceed(originalRequest)
        }

        // Get the token from the TokenProvider
        val token = TokenProvider.getToken()

        // Add the token to the request header
        val requestWithToken =  chain.request().newBuilder()
            .header("Authorization", "Bearer " +
                    "$token")
            .build()


        return chain.proceed(requestWithToken)
    }
}