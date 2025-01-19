package com.nbu.CSCB532.addressbook

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nbu.CSCB532.addressbook.api.Contact
import com.nbu.CSCB532.addressbook.api.client.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewContactActivity : AppCompatActivity() {

    private lateinit var contactNameTextView: TextView
    private lateinit var contactPhoneTextView: TextView
    private lateinit var contactEmailTextView: TextView
    private lateinit var contactAddressTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_contact)
/*

        contactNameTextView = findViewById(R.id.contactNameTextView)
        contactPhoneTextView = findViewById(R.id.contactPhoneTextView)
        contactEmailTextView = findViewById(R.id.contactEmailTextView)
        contactAddressTextView = findViewById(R.id.contactAddressTextView)
*/

        val contactId = intent.getIntExtra("CONTACT_ID", -1)

        if (contactId != -1) {
            fetchContact(contactId)
        }
    }

    private fun fetchContact(contactId: Int) {
        RetrofitClient.getInstance(this).getContact(contactId).enqueue(object : Callback<Contact> {
            override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
                if (response.isSuccessful && response.body() != null) {
                    val contact = response.body()!!
                    displayContact(contact)
                } else {
                    Toast.makeText(this@ViewContactActivity, "Failed to fetch contact details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Contact>, t: Throwable) {
                Toast.makeText(this@ViewContactActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayContact(contact: Contact) {
        contactNameTextView.text = contact.first_name + " " + contact.last_name
        contactPhoneTextView.text = contact.phone
        contactEmailTextView.text = contact.email
        //contactAddressTextView.text = contact.address
    }
}
