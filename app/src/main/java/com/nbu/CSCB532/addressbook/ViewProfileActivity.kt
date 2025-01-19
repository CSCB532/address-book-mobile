package com.nbu.CSCB532.addressbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nbu.CSCB532.addressbook.api.client.RetrofitClient
import com.nbu.CSCB532.addressbook.auth.LoginActivity
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewProfileActivity : AppCompatActivity() {

    private lateinit var editUsername: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var saveChangesButton: Button
    private lateinit var deleteAccountButton: Button
    private lateinit var returnHomeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        // Initialize views
        editUsername = findViewById(R.id.editUsername)
        editEmail = findViewById(R.id.editEmail)
        editPassword = findViewById(R.id.editPassword)
        saveChangesButton = findViewById(R.id.saveChangesButton)
        deleteAccountButton = findViewById(R.id.deleteAccountButton)
        returnHomeButton = findViewById(R.id.returnHomeButton)

        // Load profile data
        loadProfile()

        // Save changes button listener
        saveChangesButton.setOnClickListener {
            saveChanges()
        }

        // Delete account button listener
        deleteAccountButton.setOnClickListener {
            deleteAccount()
        }

        // Return home button listener
        returnHomeButton.setOnClickListener {
            navigateToHome()
        }
    }

    private fun loadProfile() {
        RetrofitClient.getInstance(this).getProfile()
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()?.string()
                        val jsonObject = JSONObject(responseBody!!)
                        val user = jsonObject.getJSONObject("user")
                        val username = user.getString("username")
                        val email = user.getString("email")

                        // Populate fields
                        editUsername.setText(username)
                        editEmail.setText(email)
                    } else {
                        Toast.makeText(
                            this@ViewProfileActivity,
                            "Failed to load profile: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("ViewProfileActivity", "Error loading profile: ${t.message}")
                    Toast.makeText(
                        this@ViewProfileActivity,
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun saveChanges() {
        val updatedUsername = editUsername.text.toString().trim()
        val updatedEmail = editEmail.text.toString().trim()
        val updatedPassword = editPassword.text.toString().trim()

        if (updatedUsername.isEmpty() || updatedEmail.isEmpty()) {
            Toast.makeText(this, "Username and email cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }

        val data = mutableMapOf("username" to updatedUsername, "email" to updatedEmail)
        if (updatedPassword.isNotEmpty()) {
            data["password"] = updatedPassword
        }

        RetrofitClient.getInstance(this).updateProfile(data)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ViewProfileActivity,
                            "Profile updated successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Update SharedPreferences with the new username
                        updateUsernameInSharedPreferences(updatedUsername)
                    } else {
                        Toast.makeText(
                            this@ViewProfileActivity,
                            "Failed to update profile: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("ViewProfileActivity", "Error updating profile: ${t.message}")
                    Toast.makeText(
                        this@ViewProfileActivity,
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun updateUsernameInSharedPreferences(updatedUsername: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", updatedUsername) // Update the username
        editor.apply()
    }

    private fun deleteAccount() {
        RetrofitClient.getInstance(this).deleteAccount()
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ViewProfileActivity,
                            "Account deleted successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Navigate to login
                        val intent = Intent(this@ViewProfileActivity, LoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@ViewProfileActivity,
                            "Failed to delete account: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("ViewProfileActivity", "Error deleting account: ${t.message}")
                    Toast.makeText(
                        this@ViewProfileActivity,
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
