package com.nbu.CSCB532.addressbook.api

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    // Login endpoint
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    // Register endpoint
    @POST("register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    // Get profile info
    @GET("profile")
    fun getProfile(): Call<ResponseBody>

    // Update profile info
    @PUT("profile")
    fun updateProfile(@Body data: Map<String, String>): Call<ResponseBody>

    // Delete account
    @DELETE("delete_account")
    fun deleteAccount(): Call<ResponseBody>

    // Get tags for the current user
    @GET("tags")
    fun getTags(): Call<TagsResponse>

    // Add a new tag
    @POST("tags")
    fun addTag(@Body tagRequest: TagRequest): Call<ResponseBody>

    @DELETE("tags/{tag_id}")
    fun deleteTag(@Path("tag_id") tagId: Int): Call<ResponseBody>
}

// Request model for adding a new tag
data class TagRequest(
    @SerializedName("name") val name: String,
    @SerializedName("color") val color: String,  // Hex color code
    @SerializedName("parent_id") val parentId: Int? // Parent tag ID, null if none
)

// Response model for tags
data class Tag(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("color") val color: String,
    @SerializedName("parent_id") val parentId: Int?
)

data class TagsResponse(
    @SerializedName("tags") val tags: List<Tag>
)

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class User(
    val id: Int,
    val email: String,
    val username: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: User
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
