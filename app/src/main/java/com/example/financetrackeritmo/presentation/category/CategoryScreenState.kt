package com.example.financetrackeritmo.presentation.category

import com.example.financetrackeritmo.domain.entity.Category

sealed class CategoryScreenState {
    data object Initial: CategoryScreenState()

    data object Loading: CategoryScreenState()

    data class Success(
        val categories: List<Category>
    ): CategoryScreenState()

    data class Error(val msg: String): CategoryScreenState()
}