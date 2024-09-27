package com.example.financetrackeritmo.presentation.transaction.item

sealed class TransactionValidateState {
    object Success : TransactionValidateState()

    class Error(val msg: String) : TransactionValidateState()
}

data class TransactionFieldsState(
    val date: TransactionValidateState,
    val amount: TransactionValidateState
)