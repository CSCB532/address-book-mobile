import android.content.Context
import com.google.gson.GsonBuilder
import com.nbu.CSCB532.addressbook.api.ApiService
import com.nbu.CSCB532.addressbook.config.ConfigUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    fun getInstance(context: Context): ApiService {
        val baseUrl = ConfigUtils.getBaseUrl(context)

        // Create a logging interceptor to log network requests and responses
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val gson = GsonBuilder()
            .setLenient() // Allow lenient parsing of malformed JSON
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)  // Add OkHttp client with logging interceptor
            .addConverterFactory(GsonConverterFactory.create(gson))  // Use the lenient Gson
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
