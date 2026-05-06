package com.aksharadeepa.tutor.di

import android.content.Context
import com.aksharadeepa.tutor.data.database.AppDatabase
import com.aksharadeepa.tutor.network.AnthropicApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideChapterDao(database: AppDatabase) = database.chapterDao()

    @Provides
    fun provideQuizDao(database: AppDatabase) = database.quizDao()

    @Provides
    fun provideGoalDao(database: AppDatabase) = database.goalDao()

    @Provides
    @Singleton
    fun provideAnthropicApi(): AnthropicApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.anthropic.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnthropicApiService::class.java)
    }
}
