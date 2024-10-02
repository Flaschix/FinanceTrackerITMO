package com.example.financetrackeritmo.domain.usecase

import com.example.financetrackeritmo.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoryNameByIdUseCase @Inject constructor(private val repository: CategoryRepository){
    suspend operator fun invoke(categoryId: Long): String {
        return repository.getCategoryNameById(categoryId)
    }
}