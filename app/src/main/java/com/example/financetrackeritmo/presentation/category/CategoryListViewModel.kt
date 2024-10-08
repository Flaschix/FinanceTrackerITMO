package com.example.financetrackeritmo.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackeritmo.domain.entity.Category
import com.example.financetrackeritmo.domain.usecase.DeleteCategoryUseCase
import com.example.financetrackeritmo.domain.usecase.GetAllCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val getAllCategoryUseCase: GetAllCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
): ViewModel() {


    private val _uiState: MutableStateFlow<CategoryListScreenState> = MutableStateFlow(CategoryListScreenState.Initial)

    val uiState: StateFlow<CategoryListScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllCategoryUseCase().map {
                CategoryListScreenState.Success(it) as CategoryListScreenState
            }.onStart {
                _uiState.value = CategoryListScreenState.Loading
            }.collect{
                _uiState.value = it
            }
        }
    }

    fun deleteCategory(category: Category){
        viewModelScope.launch {
            deleteCategoryUseCase(category)
        }
    }
}