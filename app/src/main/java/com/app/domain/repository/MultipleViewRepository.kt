package com.app.domain.repository

import com.app.domain.entity.response.Datas
import com.app.domain.entity.response.MultipleViewData
import com.app.domain.model.IOTaskResult
import com.app.domain.model.RawCallResponse
import kotlinx.coroutines.flow.Flow


interface MultipleViewRepository {

    suspend fun fetchMultipleViewData():  Flow<RawCallResponse<Datas>>?

}