package com.example.financetrackeritmo.domain.repository

import com.example.financetrackeritmo.domain.entity.Category
import kotlinx.coroutines.flow.SharedFlow

interface CategoryRepository {

    suspend fun getAllCategory(): SharedFlow<List<Category>>

    suspend fun addCategory(category: Category): Result<Unit>

    suspend fun deleteCategory(category: Category): Result<Unit>

    suspend fun updateCategory(category: Category): Result<Unit>
}