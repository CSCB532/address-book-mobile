package com.nbu.CSCB532.addressbook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.nbu.CSCB532.R
import com.nbu.CSCB532.addressbook.auth.LoginActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val logoutButton = findViewById<Button>(R.id.logout_button)
        logoutButton.setOnClickListener {
            // Clear login status
            val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()

            // Redirect to LoginActivity
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            finish()
        }
    }
}
