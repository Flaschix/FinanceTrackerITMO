package com.example.financetrackeritmo.presentation.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackeritmo.domain.usecase.AddCategoryUseCase
import com.example.financetrackeritmo.domain.usecase.DeleteCategoryUseCase
import com.example.financetrackeritmo.domain.usecase.GetAllCategoryUseCase
import com.example.financetrackeritmo.domain.usecase.UpdateCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getAllCategoryUseCase: GetAllCategoryUseCase,
): ViewModel() {


    private val _uiState: MutableStateFlow<CategoryScreenState> = MutableStateFlow(CategoryScreenState.Initial)

    val uiState: StateFlow<CategoryScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllCategoryUseCase().map {
                CategoryScreenState.Success(it) as CategoryScreenState
            }.onStart {
                _uiState.value = CategoryScreenState.Loading
            }.collect{
                _uiState.value = it
            }
        }
    }
}