package com.app.data.remote.retrofit.datasource

import com.app.data.utils.NetworkAPIInvoke
import com.app.data.utils.NetworkHandler
import com.app.domain.model.ErrorType
import com.app.domain.model.IOTaskResult
import com.app.domain.model.ViewState
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

abstract class ApiWrapper {

    @Inject
    lateinit var moshi: Moshi

    @Inject
    lateinit var networkHandler: NetworkHandler

    @Inject
    lateinit var applicationContext: ApplicationContext


//    protected suspend fun <T> getResult(call: suspend () -> Response<T>): T {
//        Timber.d("Call api")
//        if (!networkHandler.isOnline()) throw  asFailure(ErrorType.NoConnectivityError)
//        try {
//            val response = call()
//            Timber.d("response")
//            when {
//                response.code() == 403 -> {
//                    throw asFailure(ErrorType.DefaultError)
//                }
//                response.code() == 500 -> {
//                    throw asFailure(ErrorType.DefaultError)
//                }
//                response.isSuccessful -> {
//                    response.body()?.let {
//                        return it
//                    }
//                }
//                !response.isSuccessful -> {
//                    throw asFailure(ErrorType.DefaultError)
//                }
//            }
//            throw asFailure()
//        } catch (e: Exception) {
//            throw asFailure(e)
//        }
//    }


//    private fun asFailure(exception: Exception? = null): ErrorType {
//        return when (exception) {
//            null -> ErrorType.DefaultError
//            is ErrorType -> exception
//
//            is SSLHandshakeException -> {
//                ErrorType.SSLHandshakeException
//            }
//            is SocketTimeoutException -> {
//                ErrorType.SocketTimeoutException
//            }
//            is JsonEncodingException -> {
//                ErrorType.JsonEncodingException
//            }
//            is UnknownHostException -> {
//                Timber.d("No Internet")
//                ErrorType.NoConnectivityError
//            }
//            else -> ErrorType.DefaultError
//        }
//    }


    protected suspend fun <T : Any> getResult(
        allowRetries: Boolean = false,
        numberOfRetries: Int = 0,
        networkApiCall: NetworkAPIInvoke<T>
    ): Flow<IOTaskResult<T>> {
        var delayDuration = 1000L
        val delayFactor = 2
        return flow {
            if (!networkHandler.isOnline()) {
                emit(
                    IOTaskResult.OnFailed(
                        IOException("Network Error")
                    )
                )
                return@flow
            }
            val response = networkApiCall()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(IOTaskResult.OnSuccess(it))
                } ?: emit(IOTaskResult.OnFailed(IOException("empty")))
                return@flow
            }
            emit(
                IOTaskResult.OnFailed(
                    IOException("Error")
                )
            )
            return@flow
        }.catch { e ->
            emit(IOTaskResult.OnFailed(IOException("Exception ${e.message}")))
            return@catch
        }.retryWhen { cause, attempt ->
            if (!allowRetries || attempt > numberOfRetries || cause !is IOException) return@retryWhen false
            delay(delayDuration)
            delayDuration *= delayFactor
            return@retryWhen true
        }.flowOn(Dispatchers.IO)
    }

//    protected suspend fun <T : Any> getApiResultData(call: suspend () -> Response<T>): T {
//        flow {
//            emit(ViewState.Loading(true))
//            if (!networkHandler.isOnline()) {
////                throw  asFailure(ErrorType.NoConnectivityError)
//                emit(ViewState.RenderFailure(asFailure(ErrorType.NoConnectivityError)))
//                return@flow
//            }
//            val response = call()
//            when {
//                response.code() == 403 -> {
////                    throw asFailure(ErrorType.DefaultError)
//                    emit(ViewState.RenderFailure(asFailure(ErrorType.DefaultError)))
//                    return@flow
//                }
//                response.code() == 500 -> {
////                    throw asFailure(ErrorType.DefaultError)
//                    emit(ViewState.RenderFailure(asFailure(ErrorType.DefaultError)))
//                    return@flow
//                }
//                response.isSuccessful -> {
//                    response.body()?.let {
//                        emit(ViewState.RenderSuccess(it))
//                        return@flow
//                    } ?: emit(ViewState.RenderFailure(asFailure(ErrorType.DefaultError)))
//                    return@flow
//                }
//                !response.isSuccessful -> {
////                    throw asFailure(ErrorType.DefaultError)
//                    emit(ViewState.RenderFailure(asFailure(ErrorType.DefaultError)))
//                    return@flow
//                }
//            }
//            emit(ViewState.Loading(false))
//            return@flow
//        }.catch { e ->
//            emit(ViewState.RenderFailure(IOException("Exception ${e.message}")))
//            return@catch
//        }.flowOn(Dispatchers.IO)
//        throw  asFailure()
//    }
}