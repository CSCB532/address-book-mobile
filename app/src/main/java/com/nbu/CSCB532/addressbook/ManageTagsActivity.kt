package com.nbu.CSCB532.addressbook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nbu.CSCB532.addressbook.api.Tag
import com.nbu.CSCB532.addressbook.api.TagRequest
import com.nbu.CSCB532.addressbook.api.TagsResponse
import com.nbu.CSCB532.addressbook.api.client.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageTagsActivity : AppCompatActivity() {

    private lateinit var tagNameInput: EditText
    private lateinit var colorSpinner: Spinner
    private lateinit var parentTagSpinner: Spinner
    private lateinit var addTagButton: Button
    private lateinit var homeButton: Button
    private lateinit var tagsTable: TableLayout

    private var selectedColor: String = "#FFFFFF" // Default to white
    private var parentTagMap: Map<String, Int> = mapOf()  // Map of tag names to their IDs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_tags)

        // Initialize UI elements
        tagNameInput = findViewById(R.id.tagNameInput)
        colorSpinner = findViewById(R.id.colorSpinner)
        parentTagSpinner = findViewById(R.id.parentTagSpinner)
        addTagButton = findViewById(R.id.addTagButton)
        homeButton = findViewById(R.id.homeButton)
        tagsTable = findViewById(R.id.tagsTable)

        // Predefined list of colors
        val colorList = listOf(
            "#FF5733", "#33FF57", "#3357FF", "#FF33A1", "#A133FF",
            "#FF8C00", "#FFC300", "#DAF7A6", "#581845", "#900C3F",
            "#C70039", "#FF2D00", "#FFB8A1", "#00FF6A", "#72B3FF",
            "#8A2BE2", "#DC143C", "#00CED1", "#FFD700", "#FF1493"
        )

        // Set up adapter for color spinner
        val colorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colorList)
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        colorSpinner.adapter = colorAdapter

        // Set default color
        colorSpinner.setSelection(0)

        // Set the selected color when an item is selected
        colorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                selectedColor = colorList[position]
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Load tags and parent tags when the activity is created
        loadTags()
        loadParentTags()

        // Handle adding a new tag when the "Add Tag" button is clicked
        addTagButton.setOnClickListener {
            addTag()
        }

        // Handle navigating back to the Home screen
        homeButton.setOnClickListener {
            navigateToHome()
        }
    }

    // Method to load tags by sending a GET request to the API
    private fun loadTags() {
        RetrofitClient.getInstance(this).getTags()  // Call to the GET tags API
            .enqueue(object : Callback<TagsResponse> {
                override fun onResponse(call: Call<TagsResponse>, response: Response<TagsResponse>) {
                    if (response.isSuccessful) {
                        val tags = response.body()?.tags ?: emptyList()

                        // Ensure parentTagMap is loaded before displaying the tags
                        if (parentTagMap.isNotEmpty()) {
                            displayTags(tags)  // Show the tags in the TableLayout
                        } else {
                            // If parentTagMap is empty, call loadParentTags again
                            loadParentTags {
                                displayTags(tags)  // Now display the tags once parent tags are loaded
                            }
                        }
                    } else {
                        Toast.makeText(this@ManageTagsActivity, "Failed to load tags", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<TagsResponse>, t: Throwable) {
                    Toast.makeText(this@ManageTagsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Modify loadParentTags() to accept a callback to execute once the parent tags are loaded
    private fun loadParentTags(callback: (() -> Unit)? = null) {
        RetrofitClient.getInstance(this).getTags()
            .enqueue(object : Callback<TagsResponse> {
                override fun onResponse(call: Call<TagsResponse>, response: Response<TagsResponse>) {
                    if (response.isSuccessful) {
                        val tags = response.body()?.tags ?: emptyList()

                        // Create a map of tag names to their IDs
                        parentTagMap = tags.associate { it.name to it.id }

                        // Add "None" as an option for no parent tag
                        val parentTagNames = listOf("None") + parentTagMap.keys

                        // Set up the adapter for the parent tag spinner
                        val parentTagAdapter = ArrayAdapter(
                            this@ManageTagsActivity,
                            android.R.layout.simple_spinner_item,
                            parentTagNames
                        )
                        parentTagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        parentTagSpinner.adapter = parentTagAdapter

                        // Enable the spinner after data is loaded
                        parentTagSpinner.isEnabled = true

                        // Execute the callback if provided
                        callback?.invoke()
                    } else {
                        Toast.makeText(this@ManageTagsActivity, "Failed to load parent tags", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<TagsResponse>, t: Throwable) {
                    Toast.makeText(this@ManageTagsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Method to delete a tag
    private fun deleteTag(tagId: Int) {
        RetrofitClient.getInstance(this).deleteTag(tagId)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ManageTagsActivity,
                            "Tag deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Refresh the tags after deletion
                        loadTags()
                    } else {
                        Toast.makeText(
                            this@ManageTagsActivity,
                            "Failed to delete tag",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(
                        this@ManageTagsActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // Method to display tags in the TableLayout
    private fun displayTags(tags: List<Tag>) {
        // Clear existing table rows (if any)
        tagsTable.removeAllViews()

        if (tags.isEmpty()) {
            tagsTable.visibility = View.GONE
        } else {
            tagsTable.visibility = View.VISIBLE
        }

        // Add headers to the table
        val headerRow = TableRow(this)
        val headerTagName = TextView(this).apply { text = "Tag Name" }
        val headerColor = TextView(this).apply { text = "Color" }
        val headerParentTag = TextView(this).apply { text = "Parent Tag" }
        val headerDelete = TextView(this).apply { text = "Delete" }
        headerRow.addView(headerTagName)
        headerRow.addView(headerColor)
        headerRow.addView(headerParentTag)
        headerRow.addView(headerDelete)
        tagsTable.addView(headerRow)

        // Add each tag to the table
        for (tag in tags) {
            val row = TableRow(this)
            val tagName = TextView(this).apply { text = tag.name }
            val tagColor = TextView(this).apply { text = tag.color }

            // Set text color based on the tag color
            tagColor.setTextColor(android.graphics.Color.parseColor(tag.color))

            // Get the parent tag's name from the map (or "None" if null)
            val parentTagName = parentTagMap.entries.firstOrNull { it.value == tag.parentId }?.key ?: "None"
            val parentTagView = TextView(this).apply { text = parentTagName }

            // Create a delete button for the tag
            val deleteButton = Button(this).apply {
                text = "Delete"
                setBackgroundColor(android.graphics.Color.RED)  // Set the delete button color to red
                setOnClickListener {
                    deleteTag(tag.id)  // Call deleteTag() when the delete button is clicked
                }
            }

            // Add views to the row
            row.addView(tagName)
            row.addView(tagColor)
            row.addView(parentTagView)
            row.addView(deleteButton)

            // Add the row to the table
            tagsTable.addView(row)
        }
    }

    // Method to add a new tag by sending a POST request to the API
    private fun addTag() {
        val tagName = tagNameInput.text.toString()
        val selectedParentTag = parentTagSpinner.selectedItem.toString()

        // Get the parent ID from the map (null if "None" is selected)
        val parentId = if (selectedParentTag != "None") {
            parentTagMap[selectedParentTag]
        } else {
            null
        }

        val tagRequest = TagRequest(tagName, selectedColor, parentId)

        RetrofitClient.getInstance(this).addTag(tagRequest)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ManageTagsActivity,
                            "Tag added successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Reload the tags after adding a new tag
                        loadTags()

                        // Also reload the parent tags to include the new one in the spinner
                        loadParentTags()
                    } else {
                        Toast.makeText(
                            this@ManageTagsActivity,
                            "Failed to add tag",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(
                        this@ManageTagsActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // Navigate to the Home screen
    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
