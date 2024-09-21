package com.example.financetrackeritmo.domain.usecase

import com.example.financetrackeritmo.domain.entity.Category
import com.example.financetrackeritmo.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.StateFlow

class GetAllCategoryUseCase (private val repository: CategoryRepository) {
    suspend operator fun invoke(): StateFlow<List<Category>> {
        return repository.getAllCategory()
    }
}