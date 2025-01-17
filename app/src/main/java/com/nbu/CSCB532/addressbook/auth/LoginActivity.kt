package com.nbu.CSCB532.addressbook.auth


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nbu.CSCB532.R
import com.nbu.CSCB532.addressbook.HomeActivity
import com.nbu.CSCB532.addressbook.api.LoginRequest
import com.nbu.CSCB532.addressbook.api.LoginResponse
import com.nbu.CSCB532.addressbook.auth.client.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        RetrofitClient.getInstance(this).login(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse = response.body()!!
                        if (loginResponse.success) {
                            val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("isLoggedIn", true)
                            editor.apply()

                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Invalid username or password!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Error: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}