package com.example.financetrackeritmo.di

import com.example.financetrackeritmo.data.repository.CategoryRepositoryImpl
import com.example.financetrackeritmo.data.repository.TransactionRepositoryImpl
import com.example.financetrackeritmo.domain.repository.CategoryRepository
import com.example.financetrackeritmo.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun provideCategoryRepositoryImpl(repository: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    fun provideTransactionRepositoryImpl(repository: TransactionRepositoryImpl): TransactionRepository
}