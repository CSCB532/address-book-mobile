package com.nbu.CSCB532.addressbook

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nbu.CSCB532.addressbook.api.Contact
import com.nbu.CSCB532.addressbook.api.client.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditContactActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var saveButton: Button

    private var contactId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)

        /*
                nameEditText = findViewById(R.id.nameEditText)
                phoneEditText = findViewById(R.id.phoneEditText)
                emailEditText = findViewById(R.id.emailEditText)
                addressEditText = findViewById(R.id.addressEditText)
                saveButton = findViewById(R.id.saveButton)
        */

        contactId = intent.getIntExtra("CONTACT_ID", -1)

        if (contactId != -1) {
            fetchContactDetails(contactId)
        }

        saveButton.setOnClickListener {
            saveContact()
        }
    }

    private fun fetchContactDetails(contactId: Int) {
        RetrofitClient.getInstance(this).getContact(contactId).enqueue(object : Callback<Contact> {
            override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
                if (response.isSuccessful && response.body() != null) {
                    val contact = response.body()!!
                    populateFields(contact)
                } else {
                    Toast.makeText(
                        this@EditContactActivity,
                        "Failed to fetch contact details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Contact>, t: Throwable) {
                Toast.makeText(this@EditContactActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun populateFields(contact: Contact) {
        nameEditText.setText(contact.first_name + " " + contact.last_name)
        phoneEditText.setText(contact.phone)
        emailEditText.setText(contact.email)
        // addressEditText.setText(contact.address)
    }

    private fun saveContact() {
        /*val updatedContact = Contact(
            id = contactId,
            name = nameEditText.text.toString(),
            phone = phoneEditText.text.toString(),
            email = emailEditText.text.toString(),
            // address = addressEditText.text.toString()
        )
*/
        /*RetrofitClient.getInstance(this).updateContact(contactId, updatedContact)
            .enqueue(object : Callback<Contact> {
                override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@EditContactActivity,
                            "Contact updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@EditContactActivity,
                            "Failed to update contact",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Contact>, t: Throwable) {
                    Toast.makeText(
                        this@EditContactActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })*/
    }
}
