package com.app.data.module

import android.content.Context
import com.app.data.local.assets.RawDataSource
import com.app.presentation.util.CodeSnippet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RawDataSourceModule {
    @Singleton
    @Provides
    fun provideRawDataSource(
        @ApplicationContext context: Context
    ): RawDataSource {
        return RawDataSource(context)
    }
}