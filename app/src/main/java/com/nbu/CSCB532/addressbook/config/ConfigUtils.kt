package com.nbu.CSCB532.addressbook.config

import android.content.Context
import com.nbu.CSCB532.R
import java.io.IOException
import java.util.Properties

object ConfigUtils {

    private const val CONFIG_FILE = "config.properties"

    // Function to read the base URL from the properties file
    fun getBaseUrl(context: Context): String {
        val properties = Properties()
        try {
            // Load the properties file from the raw folder
            context.resources.openRawResource(R.raw.config)
                .use { inputStream ->
                    properties.load(inputStream)
                }
            // Return the base URL value from the properties file
            return properties.getProperty(
                "BASE_URL",
                "http://localhost:5000/"
            ) // default value if not found
        } catch (e: IOException) {
            e.printStackTrace()
            return "http://localhost:5000/"  // Return default URL if error occurs
        }
    }
}
