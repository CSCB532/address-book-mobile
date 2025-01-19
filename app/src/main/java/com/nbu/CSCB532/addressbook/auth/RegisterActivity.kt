package com.nbu.CSCB532.addressbook.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nbu.CSCB532.addressbook.R
import com.nbu.CSCB532.addressbook.api.RegisterRequest
import com.nbu.CSCB532.addressbook.api.RegisterResponse
import com.nbu.CSCB532.addressbook.api.client.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val email = emailInput.text.toString()
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(email, username, password)
        }
    }

    private fun registerUser(email: String, username: String, password: String) {
        val registerRequest = RegisterRequest(email, username, password)

        RetrofitClient.getInstance(this).register(registerRequest)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val registerResponse = response.body()!!
                        if (registerResponse.success) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registration successful!",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registration failed: ${registerResponse.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Error: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
