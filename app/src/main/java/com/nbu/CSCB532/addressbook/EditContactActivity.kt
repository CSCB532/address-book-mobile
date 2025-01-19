package com.nbu.CSCB532.addressbook

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nbu.CSCB532.addressbook.api.ApiResponse
import com.nbu.CSCB532.addressbook.api.Contact
import com.nbu.CSCB532.addressbook.api.Tag
import com.nbu.CSCB532.addressbook.api.TagsResponse
import com.nbu.CSCB532.addressbook.api.client.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditContactActivity : AppCompatActivity() {

    private lateinit var contactNameEditText: EditText
    private lateinit var contactPhoneEditText: EditText
    private lateinit var contactEmailEditText: EditText
    private lateinit var contactCompanyEditText: EditText
    private lateinit var contactAddressEditText: EditText
    private lateinit var contactMobileEditText: EditText
    private lateinit var contactFaxEditText: EditText
    private lateinit var contactCommentEditText: EditText
    private lateinit var contactTagsSpinner: Spinner
    private lateinit var saveButton: Button

    private lateinit var contact: Contact
    private lateinit var tagList: List<Tag>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)

        // Initialize views
        contactNameEditText = findViewById(R.id.editContactName)
        contactPhoneEditText = findViewById(R.id.editContactPhone)
        contactEmailEditText = findViewById(R.id.editContactEmail)
        contactAddressEditText = findViewById(R.id.editContactAddress)
        contactCompanyEditText = findViewById(R.id.editContactCompany)
        contactMobileEditText = findViewById(R.id.editContactMobile)
        contactFaxEditText = findViewById(R.id.editContactFax)
        contactCommentEditText = findViewById(R.id.editContactComment)
        contactTagsSpinner = findViewById(R.id.editContactTags)
        saveButton = findViewById(R.id.saveButton)

        // Get the contact data from the Intent
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

        // Populate the fields with existing data
        contactNameEditText.setText("$first_name $last_name")
        contactPhoneEditText.setText(phone.toString())
        contactEmailEditText.setText(email)
        contactAddressEditText.setText(address)
        contactCompanyEditText.setText(company_name)
        contactMobileEditText.setText(mobile.toString())
        contactFaxEditText.setText(fax)
        contactCommentEditText.setText(comment)

        // Load the tags
        loadTags()

        // Populate the Spinner with the tag list
        val tagNames = tagList.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tagNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        contactTagsSpinner.adapter = adapter

        // Set the current tag selection (if applicable)
        val currentTagName = contact.tags.firstOrNull()?.name
        currentTagName?.let {
            val tagPosition = tagNames.indexOf(it)
            contactTagsSpinner.setSelection(tagPosition)
        }

        // Save Button Listener
        saveButton.setOnClickListener {
            // Get the new data from the input fields
            val updatedName = contactNameEditText.text.toString()
            val updatedPhone = contactPhoneEditText.text.toString()
            val updatedEmail = contactEmailEditText.text.toString()
            val updatedAddress = contactAddressEditText.text.toString()
            val updatedCompany = contactCompanyEditText.text.toString()
            val updatedMobile = contactMobileEditText.text.toString()
            val updatedFax = contactFaxEditText.text.toString()
            val updatedComment = contactCommentEditText.text.toString()
            val selectedTag = contactTagsSpinner.selectedItem as String

            // Create the updated Contact object
            val updatedContact = contact.copy(
                first_name = updatedName.split(" ")[0],
                last_name = updatedName.split(" ").getOrElse(1) { "" },
                phone = updatedPhone,
                mobile = updatedMobile,
                email = updatedEmail,
                address = updatedAddress,
                company_name = updatedCompany,
                fax = updatedFax,
                comment = updatedComment,
                tags = listOf(Tag(0, selectedTag, "#000000", null)) // Assuming one tag is selected
            )

            // Pass the updated Contact back to the previous activity (or save to database)
            RetrofitClient.getInstance(this).updateContact(contactId, updatedContact)
                .enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(
                        call: Call<ApiResponse>,
                        response: Response<ApiResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@EditContactActivity,
                                "Contact updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            setResult(RESULT_OK)
                            finish()
                        } else {
                            Toast.makeText(
                                this@EditContactActivity,
                                "Failed to update contact",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Toast.makeText(
                            this@EditContactActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    private fun loadTags() {
        RetrofitClient.getInstance(this).getTags()  // Call to the GET tags API
            .enqueue(object : Callback<TagsResponse> {
                override fun onResponse(
                    call: Call<TagsResponse>,
                    response: Response<TagsResponse>
                ) {
                    if (response.isSuccessful) {
                        tagList = response.body()?.tags ?: emptyList()
                    }
                }

                override fun onFailure(call: Call<TagsResponse>, t: Throwable) {
                    Toast.makeText(
                        this@EditContactActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
