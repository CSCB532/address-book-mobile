package com.nbu.CSCB532.addressbook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ManageContactsActivity : AppCompatActivity() {

    private lateinit var addNewContactButton: Button
    private lateinit var viewContactsButton: Button
    private lateinit var mostCommonTagsButton: Button
    private lateinit var sameFirstNamesButton: Button
    private lateinit var sameLastNamesButton: Button
    private lateinit var searchByNameButton: Button
    private lateinit var returnToHomeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_contacts)

        initializeViews()

        // Handle button clicks to navigate to respective activities
        addNewContactButton.setOnClickListener {
            startActivity(Intent(this, AddContactActivity::class.java))
        }

        viewContactsButton.setOnClickListener {
            startActivity(Intent(this, ViewContactsActivity::class.java))
        }

        mostCommonTagsButton.setOnClickListener {
            startActivity(Intent(this, MostCommonTagsActivity::class.java))
        }

        sameFirstNamesButton.setOnClickListener {
            startActivity(Intent(this, SameFirstNamesActivity::class.java))
        }

        sameLastNamesButton.setOnClickListener {
            startActivity(Intent(this, SameLastNamesActivity::class.java))
        }

        searchByNameButton.setOnClickListener {
            startActivity(Intent(this, SearchByNameActivity::class.java))
        }

        returnToHomeButton.setOnClickListener {
            // Go back to the home screen
            startActivity(Intent(this, HomeActivity::class.java))
            finish()  // Close the current activity
        }
    }

    private fun initializeViews() {
        addNewContactButton = findViewById(R.id.addNewContactButton)
        viewContactsButton = findViewById(R.id.viewContactsButton)
        mostCommonTagsButton = findViewById(R.id.mostCommonTagsButton)
        sameFirstNamesButton = findViewById(R.id.sameFirstNamesButton)
        sameLastNamesButton = findViewById(R.id.sameLastNamesButton)
        searchByNameButton = findViewById(R.id.searchByNameButton)
        returnToHomeButton = findViewById(R.id.returnToHomeButton)
    }
}
