package com.nbu.CSCB532.addressbook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.nbu.CSCB532.addressbook.auth.LoginActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if the user is logged in
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            // Redirect to LoginActivity if the user is not logged in
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Close this activity to prevent the user from navigating back here
            return
        }

        // Set content view if the user is logged in
        setContentView(R.layout.activity_home)

        // Set up button click listeners
        findViewById<Button>(R.id.manageContactsButton).setOnClickListener {
            startActivity(Intent(this, ManageContactsActivity::class.java))
        }

        findViewById<Button>(R.id.importDataButton).setOnClickListener {
            startActivity(Intent(this, ImportDataActivity::class.java))
        }

        findViewById<Button>(R.id.manageTagsButton).setOnClickListener {
            startActivity(Intent(this, ManageTagsActivity::class.java))
        }

        findViewById<Button>(R.id.viewProfileButton).setOnClickListener {
            startActivity(Intent(this, ViewProfileActivity::class.java))
        }

        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        // Clear user session data
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.putString("userName", "")
        editor.apply()

        // Navigate to LoginActivity
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
