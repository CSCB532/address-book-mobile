package com.nbu.CSCB532.addressbook.api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    // Login endpoint
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    // Register endpoint
    @POST("register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>
}

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String
)

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String
)

data class RegisterResponse(
    val success: Boolean,
    val message: String
)