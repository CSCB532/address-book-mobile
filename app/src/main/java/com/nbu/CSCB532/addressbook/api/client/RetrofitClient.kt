package com.nbu.CSCB532.addressbook.auth.client

import android.content.Context
import com.nbu.CSCB532.addressbook.api.ApiService
import com.nbu.CSCB532.addressbook.config.ConfigUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    fun getInstance(context: Context): ApiService {
        // Fetch the base URL from the Config class using the context passed in
        val baseUrl = ConfigUtils.getBaseUrl(context)

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)  // Use the dynamic base URL
            .addConverterFactory(GsonConverterFactory.create())  // GsonConverterFactory for JSON conversion
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
