package com.app.data.utils

object Constants {
    const val PREF = "MyAppPref"
    const val USER_TOKEN = "user_token"
    const val USER_LOGGED_IN = "user_logged_in"
    const val FIRST_TIME_LAUNCH = "first_time_launch"
}

object HttpClient {
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L
    const val CONNECTION_TIME_OUT_MLS = CONNECT_TIMEOUT * 1000L
    const val CLIENT_TIME_OUT = 30L
}

object Authentication {
    const val MAX_RETRY = 1
}

object AppConstants {
    const val USER = "user"
    const val USER_DATA = "user_data"
    const val EMPTY = ""
}

object MultipleViewTypes {
    const val LONG_ANSWER_CODE = 1
    const val SHORT_ANSWER_CODE = 2
    const val VIDEO_CODE = 3
    const val IMAGE_CODE = 4
    const val MULTIPLE_CHOICE_CODE = 5
    const val SINGLE_CHOICE_CODE = 6
    const val DATE_CODE = 7
    const val TIME_CODE = 8
}

object MultipleViewItems {
    const val LONG_ANSWER: String = "LONG ANSWER"
    const val SHORT_ANSWER: String = "SHORT ANSWER"
    const val VIDEO: String = "VIDEO"
    const val IMAGE: String = "IMAGE"
    const val MULTIPLE_CHOICE: String = "MULTIPLE CHOICE"
    const val SINGLE_CHOICE: String = "SINGLE CHOICE"
    const val DATE: String = "DATE"
    const val TIME: String = "TIME"
}




