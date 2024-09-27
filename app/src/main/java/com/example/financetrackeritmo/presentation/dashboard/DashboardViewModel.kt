package com.example.financetrackeritmo.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackeritmo.domain.usecase.GetAllCategoryUseCase
import com.example.financetrackeritmo.domain.usecase.GetAllTransactionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val getAllCategoryUseCase: GetAllCategoryUseCase,
    private val getAllTransactionUseCase: GetAllTransactionUseCase
): ViewModel(){

    private val _uiState: MutableStateFlow<DashboardScreenState> = MutableStateFlow(
        DashboardScreenState.Initial)

    val uiState: StateFlow<DashboardScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
        }
    }
}