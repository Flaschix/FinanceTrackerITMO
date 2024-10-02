package com.example.financetrackeritmo.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackeritmo.domain.usecase.GetAllCategoryUseCase
import com.example.financetrackeritmo.domain.usecase.GetAllTransactionUseCase
import com.example.financetrackeritmo.domain.usecase.GetExpenseUseCase
import com.example.financetrackeritmo.domain.usecase.GetIncomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAllCategoryUseCase: GetAllCategoryUseCase,
    private val getAllTransactionUseCase: GetAllTransactionUseCase,
    private val getIncomeUseCase: GetIncomeUseCase,
    private val getExpenseUseCase: GetExpenseUseCase
): ViewModel(){

    private val _uiState: MutableStateFlow<DashboardScreenState> = MutableStateFlow(
        DashboardScreenState.Initial)

    val uiState: StateFlow<DashboardScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                getAllCategoryUseCase(),
                getAllTransactionUseCase(),
                getIncomeUseCase(),
                getExpenseUseCase()
            ){ categories, transactions, income, expense ->
                DashboardScreenState.Success(categories, transactions, income, expense) as DashboardScreenState
            }.onStart {
                _uiState.value = DashboardScreenState.Loading
            }.collect{
                _uiState.value = it
            }
        }
    }

    fun switchMode() {
        if(_uiState.value is DashboardScreenState.Success){
            _uiState.value = (_uiState.value as DashboardScreenState.Success).copy(
                isIncomeMode = !(_uiState.value as DashboardScreenState.Success).isIncomeMode
            )
        }
    }
}