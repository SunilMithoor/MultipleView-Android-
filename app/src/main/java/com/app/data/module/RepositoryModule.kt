package com.app.data.module


import com.app.data.repository.MultipleViewRepositoryImpl
import com.app.domain.repository.MultipleViewRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providerMultipleViewRepository(repository: MultipleViewRepositoryImpl): MultipleViewRepository {
        return repository
    }

}