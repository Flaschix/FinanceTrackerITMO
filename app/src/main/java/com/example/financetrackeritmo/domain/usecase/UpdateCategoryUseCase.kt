package com.example.financetrackeritmo.domain.usecase

import com.example.financetrackeritmo.domain.entity.Category
import com.example.financetrackeritmo.domain.repository.CategoryRepository
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(private val repository: CategoryRepository) {
    suspend operator fun invoke(category: Category): Result<Unit> {
        return repository.updateCategory(category)
    }
}