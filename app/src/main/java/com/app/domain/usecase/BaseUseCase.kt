package com.app.domain.usecase

import com.app.domain.model.IOTaskResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow


//abstract class BaseUseCase<Params, T> {
//
//    protected abstract suspend fun buildUseCase(params: Params): T
//
//    suspend fun run(params: Params, onSuccess: (T) -> Unit, onError: (ErrorType?) -> Unit) {
//
//        try {
//            val result = buildUseCase(params)
//            onSuccess(result)
//        } catch (failure: ErrorType) {
//            onError(failure)
//        }
//    }
//
//    object None {
//        override fun toString() = "UseCase.None"
//    }
//}

object None {
    override fun toString() = "UseCase.None"
}

interface IUseCase<in I : Any, out O : Any> {

    /**
     * Execution contract which will run the business logic associated with completing a
     * particular use case
     * @param input [I] type input parameter
     * @since 1.0
     * @return [O] model type used to define the UseCase class
     */
    @ExperimentalCoroutinesApi
    suspend fun execute(input: I): Flow<IOTaskResult<O>>
}


interface IUseCase3<in I : Any, out O : Any> {

    /**
     * Execution contract which will run the business logic associated with completing a
     * particular use case
     * @param input [I] type input parameter
     * @since 1.0
     * @return [O] model type used to define the UseCase class
     */
    @ExperimentalCoroutinesApi
    suspend fun execute(input: I): O?
}
