package com.app.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NetworkHandler
@Inject constructor(@ApplicationContext val context: Context) {

    fun isNetworkAvailable(): Boolean {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    fun isOnline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val n = cm.activeNetwork
            if (n != null) {
                val nc = cm.getNetworkCapabilities(n)
                //It will check for both wifi and cellular network
                return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        nc.hasTransport(
                            NetworkCapabilities.TRANSPORT_WIFI
                        )
            }
            return false
        } else {
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }
    }


}