package com.nbu.CSCB532.addressbook.api.client

import android.content.Context
import com.google.gson.GsonBuilder
import com.nbu.CSCB532.addressbook.api.ApiService
import com.nbu.CSCB532.addressbook.config.ConfigUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    fun getInstance(context: Context): ApiService {
        val baseUrl = ConfigUtils.getBaseUrl(context)

        // Create a logging interceptor to log network requests and responses
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        // Create an Interceptor to add the session cookie to request headers
        val sessionCookieInterceptor = Interceptor { chain ->
            val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val sessionCookie = sharedPreferences.getString("session_cookie", null)

            // Build the request with the session cookie if it exists
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            sessionCookie?.let {
                requestBuilder.addHeader("Cookie", it) // Add session cookie to the request header
            }
            chain.proceed(requestBuilder.build())
        }

        // Create the OkHttp client and add interceptors
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Logging interceptor
            .addInterceptor(sessionCookieInterceptor) // Session cookie interceptor
            .build()

        // Create Gson with lenient parsing
        val gson = GsonBuilder()
            .setLenient() // Allow lenient parsing of malformed JSON
            .create()

        // Build Retrofit instance with OkHttp client and Gson converter
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)  // Add OkHttp client with interceptors
            .addConverterFactory(GsonConverterFactory.create(gson))  // Use the lenient Gson
            .build()

        // Return the ApiService
        return retrofit.create(ApiService::class.java)
    }
}
