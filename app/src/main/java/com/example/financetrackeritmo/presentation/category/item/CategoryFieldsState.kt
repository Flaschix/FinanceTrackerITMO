package com.example.financetrackeritmo.presentation.category.item

sealed class CategoryValidateState {
    object Success: CategoryValidateState()

    class Error(val msg: String): CategoryValidateState()
}

data class CategoryFieldsState(
    val name: CategoryValidateState,
)