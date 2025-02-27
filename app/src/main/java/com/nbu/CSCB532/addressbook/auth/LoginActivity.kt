package com.nbu.CSCB532.addressbook.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nbu.CSCB532.addressbook.HomeActivity
import com.nbu.CSCB532.addressbook.R
import com.nbu.CSCB532.addressbook.api.LoginRequest
import com.nbu.CSCB532.addressbook.api.LoginResponse
import com.nbu.CSCB532.addressbook.api.User
import com.nbu.CSCB532.addressbook.api.client.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var loadingSpinner: ProgressBar
    private lateinit var registerLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews()

        // Navigate to the register screen when clicking the register link
        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Handle login button click
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Please enter email and password")
            } else {
                loginUser(email, password)
            }
        }
    }

    private fun initializeViews() {
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.login_button)
        registerLink = findViewById(R.id.register_link)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        // Make the login API call using Retrofit
        RetrofitClient.getInstance(this).login(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    // Log the response body to check for unexpected content
                    val responseBody = response.body()?.toString() ?: response.errorBody()?.string()
                    Log.d("LoginActivity", "Response: $responseBody")

                    // Check if the response was successful
                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse = response.body()!!

                        // Store session cookie if login is successful
                        if (loginResponse.success) {
                            // Get the cookies (session ID) from response headers
                            val sessionCookie = response.headers()["Set-Cookie"]
                            if (sessionCookie != null) {
                                // Store the session cookie
                                saveSessionCookie(sessionCookie)
                            }

                            // Save login state and user details
                            saveLoginState(true, loginResponse.user)
                            navigateToHome()
                        } else {
                            showToast(loginResponse.message)
                        }
                    } else {
                        // Handle non-JSON response (plain text, HTML, etc.)
                        var errorMessage = response.message() ?: "Unknown error"
                        showToast("Error: $errorMessage")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("LoginActivity", "Network error: ${t.message}")
                    showToast("Network error: ${t.message}")
                }
            })
    }

    private fun saveSessionCookie(cookie: String) {
        // Store the session cookie in SharedPreferences
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("session_cookie", cookie)
        editor.apply()
    }

    private fun saveLoginState(isLoggedIn: Boolean, user: User) {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.putString("username", user.username)
        editor.apply()
    }

    private fun navigateToHome() {
        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
        finish()
    }
}
