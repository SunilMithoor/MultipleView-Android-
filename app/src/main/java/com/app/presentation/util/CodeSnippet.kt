package com.app.presentation.util

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat
import java.util.regex.Pattern


class CodeSnippet(val context: Context) {

    fun isValidMail(email: String?): Boolean {
        val emailPattern = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        return Pattern.compile(emailPattern).matcher(email?.trim()).matches()
    }

    fun isValidMobile(phone: String?): Boolean {
        return if (!Pattern.matches("[a-zA-Z]+", phone)) {
            phone?.length == 10
        } else false
    }

    fun hasNetworkConnection(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var isConnected = false
        // Retrieve current status of connectivity
        connectivityManager.allNetworks.forEach { network ->
            val networkCapability = connectivityManager.getNetworkCapabilities(network)
            networkCapability?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    isConnected = true
                    return@forEach
                }
            }
        }
        return isConnected
    }


    fun getContexts() : Context{
        return context
    }

    fun getDrawables(drawable: Int) : Drawable?{
        return ContextCompat.getDrawable(context, drawable)
    }

    fun getColors(drawable: Int) : Int?{
        return ContextCompat.getColor(context, drawable)
    }

    fun getStrings(drawable: Int) : String?{
        return context.getString(drawable)
    }

    fun getResources() : Resources {
        return context.resources
    }

    fun checkPassword(password: String): Boolean {
        val passwordREGEX = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{7,}" +               //at least 7 characters
                    "$"
        );
        return passwordREGEX.matcher(password).matches()
    }

    fun checkDecimal(data: String) : Boolean{
        val regex = "\\d*\\.?\\d+"
        println(data.trim().matches(regex.toRegex()).toString())
        return data.trim().matches(regex.toRegex())
    }

    fun checkPhone(data: String) : Boolean {
        val regex = "[0-9]+"
        return data.trim().matches(regex.toRegex())
    }
}