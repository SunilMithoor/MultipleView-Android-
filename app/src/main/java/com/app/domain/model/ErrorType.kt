package com.app.domain.model

import java.io.IOException


sealed class ErrorType : IOException() {
    object NoConnectivityError : ErrorType()
    object SocketTimeoutException : ErrorType()
    object SSLHandshakeException : ErrorType()
    object JsonEncodingException : ErrorType()
    object DefaultError : ErrorType()
    class ServerError(var status: Int, var code: Int = 0, var desc: String) : ErrorType()
    class FormValidationError(var desc: String) : ErrorType()
}


