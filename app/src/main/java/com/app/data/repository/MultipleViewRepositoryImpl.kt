package com.app.data.repository

import com.app.data.local.assets.RawDataSource
import com.app.domain.repository.MultipleViewRepository
import com.app.domain.usecase.None
import javax.inject.Inject


class MultipleViewRepositoryImpl @Inject constructor(
    private val rawDataSource: RawDataSource
) : MultipleViewRepository {

    override suspend fun fetchMultipleViewData() =
        rawDataSource.readData()


}
