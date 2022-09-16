package com.app.data.remote.retrofit.interceptors


import android.content.Context
import android.os.Build
import androidx.multidex.BuildConfig.VERSION_CODE
import androidx.multidex.BuildConfig.VERSION_NAME
import okhttp3.*
import okio.IOException
import timber.log.Timber
import javax.inject.Inject


class SupportInterceptor @Inject constructor() :
    Interceptor,
    Authenticator {

    /**
     * Interceptor class for setting of the headers for every request
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build()
        return chain.proceed(request)
    }

    /**
     * Authenticator for when the authToken need to be refresh and updated
     * everytime we get a 401 error code
     */
    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? {
        var requestAvailable: Request? = null
        try {
//            val token = appSharedPreferences.getToken()
            val token = ""
            requestAvailable = response.request?.newBuilder()
                .addHeader("Authorization", token)
                .build()
            return requestAvailable
        } catch (ex: Exception) {
        }
        return requestAvailable
    }

}

class UserAgentInterceptor @Inject constructor(
    private val context: Context
) :
    Interceptor {

    private val userAgent: String by lazy {
        buildUserAgent(context)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        builder.header("User-Agent", userAgent)
        val httpUrl = request.url.newBuilder().setQueryParameter("locale", "en").build()
        builder.url(httpUrl)
        Timber.d("API call")
//        Timber.d("logged in ${appSharedPreferences.isUserLoggedIn()}")
//        if (appSharedPreferences.isUserLoggedIn()) {
//            val token = appSharedPreferences.getToken()
//            Timber.d("token $token")
//            builder.header("Authorization", token)
//        }
        return chain.proceed(builder.build())
    }

    private fun buildUserAgent(context: Context): String {
        val versionName = VERSION_NAME
        val versionCode = VERSION_CODE
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        val version = Build.VERSION.SDK_INT
        val versionRelease = Build.VERSION.RELEASE
        val installerName = context.packageName
        return "$installerName / $versionName ($versionCode); ($manufacturer; $model; SDK $version; Android $versionRelease)"
    }
}

