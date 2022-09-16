package com.app.domain.util

import com.app.domain.model.IOTaskResult
import com.app.domain.model.RawCallResponse
import com.app.domain.model.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

/**
 * Util method that takes a suspend function returning a [Flow] of [IOTaskResult] as input param and returns a
 * [Flow] of [ViewState], which emits [ViewState.Loading] with true prior to performing the IO Task. If the
 * IO operation results a [IOTaskResult.OnSuccess], the result is mapped to a [ViewState.RenderSuccess] instance and emitted,
 * else a [IOTaskResult.OnFailed] is mapped to a [ViewState.RenderFailure] instance and emitted.
 * The flowable is then completed by emitting a [ViewState.Loading] with false
 */
@ExperimentalCoroutinesApi
suspend fun <T : Any> getViewStateFlowForNetworkCall(ioOperation: suspend () -> Flow<IOTaskResult<T>>) =
    flow {
        emit(ViewState.Loading(true))
        ioOperation().map {
            when (it) {
                is IOTaskResult.OnSuccess -> ViewState.RenderSuccess(it.data)
                is IOTaskResult.OnFailed -> ViewState.RenderFailure(it.throwable)
            }
        }.collect {
            emit(it)
        }
        emit(ViewState.Loading(false))
    }.flowOn(Dispatchers.IO)

suspend fun <T : Any> getViewStateFlowForAll(ioOperation: suspend () -> Flow<T>) =
    flow {
        emit(ViewState.Loading(true))
        ioOperation().map {
            it
        }.collect {
            emit(it)
        }
        emit(ViewState.Loading(false))
    }.flowOn(Dispatchers.IO)

@ExperimentalCoroutinesApi
suspend fun <T : Any> getViewStateFlowForRawCall(ioOperation: suspend () -> Flow<RawCallResponse<T>>) =
    flow {
        emit(ViewState.Loading(true))
        ioOperation().map {
            when (it) {
                is RawCallResponse.OnSuccess -> ViewState.RenderSuccess(it.data)
                is RawCallResponse.OnFailed -> ViewState.RenderFailure(it.throwable)
            }
        }.collect {
            emit(it)
        }
        emit(ViewState.Loading(false))
    }.flowOn(Dispatchers.IO)




