package com.app.presentation.module

import android.content.Context
import com.app.presentation.util.CodeSnippet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CodeSnippetModule {
    @Singleton
    @Provides
    fun provideCodeSnippet(
        @ApplicationContext context: Context
    ): CodeSnippet {
        return CodeSnippet(context)
    }
}