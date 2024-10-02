package com.example.financetrackeritmo.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackeritmo.domain.entity.Transaction
import com.example.financetrackeritmo.domain.usecase.DeleteTransactionUseCase
import com.example.financetrackeritmo.domain.usecase.GetAllTransactionUseCase
import com.example.financetrackeritmo.domain.usecase.GetCategoryNameByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val getAllTransactionUseCase: GetAllTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val getCategoryNameByIdUseCase: GetCategoryNameByIdUseCase
) : ViewModel() {


    private val _uiState: MutableStateFlow<TransactionListScreenState> = MutableStateFlow(
        TransactionListScreenState.Initial
    )

    val uiState: StateFlow<TransactionListScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllTransactionUseCase().map {
                TransactionListScreenState.Success(it)
            }.onStart {
                _uiState.value = TransactionListScreenState.Loading
            }.collect {
                _uiState.value = it
            }
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            deleteTransactionUseCase(transaction)
        }
    }

    suspend fun getCategoryNameById(categoryId: Long): String {
        return getCategoryNameByIdUseCase(categoryId = categoryId)
    }
}