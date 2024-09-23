package com.example.financetrackeritmo.di

import android.content.Context
import androidx.room.Room
import com.example.financetrackeritmo.data.dao.CategoryDao
import com.example.financetrackeritmo.data.db.TrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideCategoryDatabase(@ApplicationContext context: Context): TrackerDatabase{
        return Room.databaseBuilder(
            context,
            TrackerDatabase::class.java,
            "trackerDatabase.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(trackerDatabase: TrackerDatabase): CategoryDao{
        return trackerDatabase.getCategoryDao()
    }
}