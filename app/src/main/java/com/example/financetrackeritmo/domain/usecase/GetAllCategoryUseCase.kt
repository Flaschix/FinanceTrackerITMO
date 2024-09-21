package com.example.financetrackeritmo.domain.usecase

import com.example.financetrackeritmo.domain.entity.Category
import com.example.financetrackeritmo.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetAllCategoryUseCase @Inject constructor(private val repository: CategoryRepository) {
    suspend operator fun invoke(): SharedFlow<List<Category>> {
        return repository.getAllCategory()
    }
}