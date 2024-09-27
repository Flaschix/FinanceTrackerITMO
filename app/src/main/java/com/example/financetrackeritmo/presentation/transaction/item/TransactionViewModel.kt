package com.example.financetrackeritmo.presentation.transaction.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackeritmo.domain.entity.Transaction
import com.example.financetrackeritmo.domain.usecase.AddTransactionUseCase
import com.example.financetrackeritmo.domain.usecase.UpdateTransactionUseCase
import com.example.financetrackeritmo.presentation.category.item.CategoryValidateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase
) : ViewModel() {

    private val _validation = Channel<TransactionFieldsState>()
    val validateState = _validation.receiveAsFlow()

    fun saveTransaction(categoryName: String, date: String, amount: String, note: String) {
        if (validateTransaction(date, amount)) {

            viewModelScope.launch {
                val result = addTransactionUseCase(
                    Transaction(
                        categoryName = categoryName,
                        date = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        amount = amount.toDouble(),
                        note = note
                    )
                )
                if (result.isFailure) transactionError()

            }

        } else failedValidation(date, amount)
    }

    fun editTransaction(
        id: Long,
        categoryName: String,
        date: String,
        amount: String,
        note: String
    ) {
        if (validateTransaction(date, amount)) {

            viewModelScope.launch {
                val result = updateTransactionUseCase(
                    Transaction(
                        id = id,
                        categoryName = categoryName,
                        date = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        amount = amount.toDouble(),
                        note = note
                    )
                )
                if (result.isFailure) transactionError()
            }

        } else failedValidation(date, amount)

    }

    private fun validateName(name: String): CategoryValidateState {
        if (name.isEmpty()) return CategoryValidateState.Error("Fill this field")
        if (name.startsWith(" ") || name.endsWith(" ")) return CategoryValidateState
            .Error("the title cannot contain a space at the beginning or at the end")

        return CategoryValidateState.Success
    }

    private fun failedValidation(date: String, amount: String) {
        val fieldsState = TransactionFieldsState(
            validateDate(date),
            validateAmount(amount)
        )

        viewModelScope.launch {
            _validation.send(fieldsState)
        }
    }


    fun validateTransaction(date: String, amount: String): Boolean {
        val dateValidation = validateDate(date)
        val amountValidation = validateAmount(amount)

        return dateValidation is TransactionValidateState.Success &&
                amountValidation is TransactionValidateState.Success
    }

    private fun validateDate(date: String): TransactionValidateState {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } catch (ex: Exception) {
            return TransactionValidateState.Error("Date corrupted")
        }
        return TransactionValidateState.Success
    }

    private fun validateAmount(amount: String): TransactionValidateState {
        return if (amount.isNotEmpty() && amount.toDoubleOrNull() != null) {
            TransactionValidateState.Success
        } else {
            TransactionValidateState.Error("Amount must be a valid number")
        }
    }

    private fun transactionError() {
        viewModelScope.launch {
            _validation.send(
                TransactionFieldsState(
                    TransactionValidateState.Error("Check the Date"),
                    TransactionValidateState.Error("Check the Amount"),
                )
            )
        }
    }

}