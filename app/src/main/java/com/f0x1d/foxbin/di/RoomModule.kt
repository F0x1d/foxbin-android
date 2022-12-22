package com.f0x1d.foxbin.di

import android.app.Application
import androidx.room.Room
import com.f0x1d.foxbin.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application) = Room
        .databaseBuilder(application, AppDatabase::class.java, "notes-cache")
        .build()
}