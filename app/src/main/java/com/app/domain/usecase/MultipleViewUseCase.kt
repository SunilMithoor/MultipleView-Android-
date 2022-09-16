package com.app.domain.usecase

import com.app.domain.entity.response.Datas
import com.app.domain.entity.response.MultipleViewData
import com.app.domain.model.IOTaskResult
import com.app.domain.model.RawCallResponse
import com.app.domain.repository.MultipleViewRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MultipleViewUseCase @Inject constructor(private val multipleViewRepository: MultipleViewRepository) :
    IUseCase3<None, Flow<RawCallResponse<Datas>>> {

    @ExperimentalCoroutinesApi
    override suspend fun execute(input: None):  Flow<RawCallResponse<Datas>>? =
        multipleViewRepository.fetchMultipleViewData()
}