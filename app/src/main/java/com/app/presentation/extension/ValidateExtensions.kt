package com.app.presentation.extension

fun String?.isValidEmail(): Boolean? {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    return this?.trim()?.matches(emailPattern.toRegex())
}


fun String?.validMobile(): Boolean {
    if (this != null) {
        return this.length < 10
    }
    return false
}

