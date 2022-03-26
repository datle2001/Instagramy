package depauw.datle.instagramy

import android.app.Application
import com.parse.Parse
import com.parse.ParseObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class InstagramyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        ParseObject.registerSubclass(Post::class.java)

        // Use for monitoring Parse network traffic
        val builder = OkHttpClient.Builder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        //httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        // any network interceptors must be added with the Configuration Builder given this syntax
        //builder.networkInterceptors().add(httpLoggingInterceptor)


        // Set applicationId and server based on the values in the Back4App settings.
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(resources.getString(R.string.back4app_app_id))
                .clientKey(resources.getString(R.string.back4app_client_key))
                .server(resources.getString(R.string.back4app_server_url)).build()
        )
    }
}