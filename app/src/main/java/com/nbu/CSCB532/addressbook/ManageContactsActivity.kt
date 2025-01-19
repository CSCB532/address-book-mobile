package com.nbu.CSCB532.addressbook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nbu.CSCB532.addressbook.api.Contact
import com.nbu.CSCB532.addressbook.api.ContactResponse
import com.nbu.CSCB532.addressbook.api.client.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageContactsActivity : AppCompatActivity() {

    private lateinit var addNewContactButton: Button
    private lateinit var viewContactsButton: Button
    private lateinit var mostCommonTagsButton: Button
    private lateinit var sameFirstNamesButton: Button
    private lateinit var sameLastNamesButton: Button
    private lateinit var searchByNameButton: Button
    private lateinit var returnToHomeButton: Button

    private lateinit var contactsTable: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_contacts)

        initializeViews()

        // Fetch contacts on activity creation
        fetchContacts()
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
        contactsTable = findViewById(R.id.contactsTable)

        addNewContactButton = findViewById(R.id.addNewContactButton)
        viewContactsButton = findViewById(R.id.viewContactsButton)
        mostCommonTagsButton = findViewById(R.id.mostCommonTagsButton)
        sameFirstNamesButton = findViewById(R.id.sameFirstNamesButton)
        sameLastNamesButton = findViewById(R.id.sameLastNamesButton)
        searchByNameButton = findViewById(R.id.searchByNameButton)
        returnToHomeButton = findViewById(R.id.returnToHomeButton)
    }

    private fun fetchContacts() {
        RetrofitClient.getInstance(this).getContacts().enqueue(object : Callback<ContactResponse> {
            override fun onResponse(call: Call<ContactResponse>, response: Response<ContactResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val contacts = response.body()!!.contacts
                    displayContacts(contacts)
                } else {
                    Toast.makeText(this@ManageContactsActivity, "Failed to load contacts", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ContactResponse>, t: Throwable) {
                Toast.makeText(this@ManageContactsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Method to display contacts in the TableLayout
    private fun displayContacts(contacts: List<Contact>) {
        contactsTable.removeAllViews()

        if (contacts.isEmpty()) {
            contactsTable.visibility = View.GONE
        } else {
            contactsTable.visibility = View.VISIBLE
        }

        // Add headers
        val headerRow = TableRow(this)
        val headerName = TextView(this).apply { text = "Name" }
        val headerPhone = TextView(this).apply { text = "Phone" }
        val headerEmail = TextView(this).apply { text = "Email" }
        val headerActions = TextView(this).apply { text = "Actions" }
        headerRow.addView(headerName)
        headerRow.addView(headerPhone)
        headerRow.addView(headerEmail)
        headerRow.addView(headerActions)
        contactsTable.addView(headerRow)

        // Add each contact row
        for (contact in contacts) {
            val row = TableRow(this)
            val contactName = TextView(this).apply { text = contact.first_name + " " + contact.last_name }
            val contactPhone = TextView(this).apply { text = contact.phone }
            val contactEmail = TextView(this).apply { text = contact.email }

            // View, Edit, and Delete buttons
// Create Buttons with proper layout
            val viewButton = Button(this).apply {
                text = "View"
                layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)  // Set equal width
                setOnClickListener { viewContact(contact) }
            }

            val editButton = Button(this).apply {
                text = "Edit"
                layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)  // Set equal width
                setOnClickListener { editContact(contact) }
            }

            val deleteButton = Button(this).apply {
                text = "Delete"
                layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)  // Set equal width
                setBackgroundColor(android.graphics.Color.RED)
                setOnClickListener { deleteContact(contact.id) }
            }

            // Add views to row
            row.addView(contactName)
            row.addView(contactPhone)
            row.addView(contactEmail)
            row.addView(viewButton)
            row.addView(editButton)
            row.addView(deleteButton)

            // Add row to table
            contactsTable.addView(row)
        }
    }

    // Handle View button click
    private fun viewContact(contact: Contact) {
        val intent = Intent(this, ViewContactActivity::class.java).apply {
            putExtra("CONTACT_ID", contact.id)
        }
        startActivity(intent)
    }

    // Handle Edit button click
    private fun editContact(contact: Contact) {
        val intent = Intent(this, EditContactActivity::class.java).apply {
            putExtra("CONTACT_ID", contact.id)
        }
        startActivity(intent)
    }

    // Handle Delete button click
    private fun deleteContact(contactId: Int) {
        RetrofitClient.getInstance(this).deleteContact(contactId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ManageContactsActivity, "Contact deleted", Toast.LENGTH_SHORT).show()
                    fetchContacts() // Reload the contact list
                } else {
                    Toast.makeText(this@ManageContactsActivity, "Failed to delete contact", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@ManageContactsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
