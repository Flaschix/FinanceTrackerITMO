package com.example.financetrackeritmo.domain.repository

import com.example.financetrackeritmo.domain.entity.Category
import kotlinx.coroutines.flow.StateFlow

interface CategoryRepository {

    suspend fun getAllCategory(): StateFlow<List<Category>>

    suspend fun addCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun updateCategory(category: Category)
}