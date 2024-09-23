package com.example.financetrackeritmo.presentation.category

import com.example.financetrackeritmo.domain.entity.Category

sealed class CategoryListScreenState {
    data object Initial: CategoryListScreenState()

    data object Loading: CategoryListScreenState()

    data class Success(
        val categories: List<Category>
    ): CategoryListScreenState()

    data class Error(val msg: String): CategoryListScreenState()
}