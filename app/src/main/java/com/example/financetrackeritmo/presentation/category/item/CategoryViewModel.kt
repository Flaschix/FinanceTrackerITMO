package com.example.financetrackeritmo.presentation.category.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackeritmo.domain.entity.Category
import com.example.financetrackeritmo.domain.entity.TransactionType
import com.example.financetrackeritmo.domain.usecase.AddCategoryUseCase
import com.example.financetrackeritmo.domain.usecase.GetAllCategoryUseCase
import com.example.financetrackeritmo.domain.usecase.UpdateCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
): ViewModel() {

    fun addNewCategory(name: String, type: TransactionType){
        viewModelScope.launch {
            addCategoryUseCase(Category(name = name, type = type))
        }
    }

    fun editCategory(name: String, type: TransactionType){
        viewModelScope.launch {
            updateCategoryUseCase(Category(name = name, type = type))
        }
    }
}