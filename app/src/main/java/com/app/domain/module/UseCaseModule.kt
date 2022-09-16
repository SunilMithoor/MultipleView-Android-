package com.app.domain.module

import com.app.domain.repository.MultipleViewRepository
import com.app.domain.usecase.MultipleViewUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object UseCaseModule {

    /**
     * Returns a [MultipleViewUseCase] instance
     * @param repository [MultipleViewRepository] impl
     * @since 1.0.0
     */
    @Provides
    fun provideMultipleViewUseCase(repository: MultipleViewRepository): MultipleViewUseCase =
        MultipleViewUseCase(
            repository
        )


}