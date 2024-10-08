package com.example.financetrackeritmo.presentation.transaction

import com.example.financetrackeritmo.domain.entity.Transaction


sealed class TransactionListScreenState {
    data object Initial: TransactionListScreenState()

    data object Loading: TransactionListScreenState()

    data class Success(
        val transactions: List<Transaction>
    ): TransactionListScreenState()

    data class Error(val msg: String): TransactionListScreenState()
}