package com.nbu.CSCB532.addressbook

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nbu.CSCB532.addressbook.R

class ImportDataActivity : AppCompatActivity() {

    private lateinit var selectFileTextView: TextView
    private lateinit var importButton: Button
    private lateinit var backToHomeButton: Button
    private var selectedFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_data)

        // Initialize views
        selectFileTextView = findViewById(R.id.selectFileTextView)
        importButton = findViewById(R.id.importButton)
        backToHomeButton = findViewById(R.id.backToHomeButton)

        // Handle file selection when the TextView is clicked
        selectFileTextView.setOnClickListener {
            // Launch the file picker with MIME types limited to CSV and JSON
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*" // Allow all file types

            // Set MIME types to limit file selection to CSV and JSON
            intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("text/csv", "application/json"))

            startActivityForResult(intent, FILE_SELECT_CODE)
        }

        // Handle import button
        importButton.setOnClickListener {
            if (selectedFileUri != null) {
                // Proceed with importing the file (implement logic here)
                Toast.makeText(this, "File selected: $selectedFileUri", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please select a file first.", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle back to home button
        backToHomeButton.setOnClickListener {
            finish() // Or navigate to the home activity
        }
    }

    // Handle the file selected from the file picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
            // Get the URI of the selected file
            selectedFileUri = data?.data
            // Display the file name in the TextView
            selectFileTextView.text = selectedFileUri?.path?.substringAfterLast("/") ?: "File selected"
        }
    }

    companion object {
        const val FILE_SELECT_CODE = 1000
    }
}
