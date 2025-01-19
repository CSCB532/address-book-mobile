package com.nbu.CSCB532.addressbook.api

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.POST
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

    // Get contacts for the current user
    @GET("contacts")
    fun getContacts(): Call<ContactResponse>

    // View a specific contact by ID
    @GET("contacts/{contact_id}")
    fun getContact(@Path("contact_id") contactId: Int): Call<Contact>

    // Edit a specific contact by ID
    @PUT("contacts/{contact_id}")
    fun updateContact(@Path("contact_id") contactId: Int, @Body contact: Contact): Call<ApiResponse>

    // Delete a specific contact by ID
    @DELETE("contacts/{contact_id}")
    fun deleteContact(@Path("contact_id") contactId: Int): Call<ResponseBody>
}
data class ApiResponse(
    val success: Boolean,
    val message: String
)

data class Contact(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val company_name: String,
    val phone: String,
    val mobile: String?,
    val email: String,
    val fax: String?,
    val address: String,
    val comment: String?,
    val custom_fields: Map<String, Any>, // If the custom fields are dynamic
    val tags: List<Tag> // List of Tag objects
)

data class ContactResponse(
    val success: Boolean,
    val contacts: List<Contact>
)

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
