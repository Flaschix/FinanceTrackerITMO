package com.example.financetrackeritmo.domain.usecase

import com.example.financetrackeritmo.domain.entity.Category
import com.example.financetrackeritmo.domain.repository.CategoryRepository

class DeleteCategoryUseCase (private val repository: CategoryRepository) {
    suspend operator fun invoke(category: Category) {
        repository.deleteCategory(category)
    }
}