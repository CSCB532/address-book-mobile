package com.nbu.CSCB532.addressbook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewContactActivity : AppCompatActivity() {

    private lateinit var contactNameTextView: TextView
    private lateinit var contactCompanyTextView: TextView
    private lateinit var contactAddressTextView: TextView
    private lateinit var contactPhoneTextView: TextView
    private lateinit var contactEmailTextView: TextView
    private lateinit var contactFaxTextView: TextView
    private lateinit var contactMobileTextView: TextView
    private lateinit var contactCommentTextView: TextView
    private lateinit var editButton: Button
    private lateinit var backToContactsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_contact)

        // Initialize views
        contactNameTextView = findViewById(R.id.contactName)
        contactCompanyTextView = findViewById(R.id.contactCompany)
        contactAddressTextView = findViewById(R.id.contactAddress)
        contactPhoneTextView = findViewById(R.id.contactPhone)
        contactEmailTextView = findViewById(R.id.contactEmail)
        contactFaxTextView = findViewById(R.id.contactFax)
        contactMobileTextView = findViewById(R.id.contactMobile)
        contactCommentTextView = findViewById(R.id.contactComment)
        editButton = findViewById(R.id.editButton)
        backToContactsButton = findViewById(R.id.backToContactsButton)

        // Get the contactId from intent
        val contactId = intent.getIntExtra("CONTACT_ID", -1)

        val first_name = intent.getStringExtra("first_name")
        val last_name = intent.getStringExtra("last_name")
        val company_name = intent.getStringExtra("company_name")
        val phone = intent.getIntExtra("phone", -1)
        val mobile = intent.getIntExtra("mobile", -1)
        val email = intent.getStringExtra("email")
        val fax = intent.getStringExtra("fax")
        val address = intent.getStringExtra("address")
        val comment = intent.getStringExtra("comment")
        contactNameTextView.text = "Name: ${first_name} ${last_name}"
        contactCompanyTextView.text = "Company: ${company_name ?: "N/A"}"
        contactAddressTextView.text = "Address: ${address ?: "N/A"}"
        contactPhoneTextView.text = "Phone: ${phone}"
        contactEmailTextView.text = "Email: ${email}"
        contactFaxTextView.text = "Fax: ${fax ?: "N/A"}"
        contactMobileTextView.text = "Mobile: ${mobile ?: "N/A"}"
        contactCommentTextView.text = "Comment: ${comment ?: "N/A"}"

        // Edit button click listener
        editButton.setOnClickListener {
            val editIntent = Intent(this, EditContactActivity::class.java)
            editIntent.putExtra(
                "CONTACT_ID",
                contactId
            ) // Pass the contactId to the EditContactActivity
            startActivity(editIntent)
        }

        backToContactsButton.setOnClickListener {
            val intent = Intent(this, ManageContactsActivity::class.java)
            startActivity(intent)
            finish() // Optional: Finish the current activity to remove it from the back stack
        }
    }
}
