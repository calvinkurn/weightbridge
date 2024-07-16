package com.example.weightbridge.di.module

import android.content.Context
import com.example.weightbridge.domain.repository.FirebaseRepository
import com.example.weightbridge.domain.repository.FirebaseRepositoryImpl
import com.example.weightbridge.domain.repository.PreferenceRepository
import com.example.weightbridge.domain.repository.PreferenceRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule(private val context: Context) {
    @Provides
    @Singleton
    fun provideContext(): Context {
        return context.applicationContext
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(): FirebaseRepository {
        return FirebaseRepositoryImpl()
    }

    @Provides
    @Singleton
    fun providePreferenceRepository(): PreferenceRepository {
        return PreferenceRepositoryImpl()
    }
}