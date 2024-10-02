package com.example.financetrackeritmo.presentation.transaction.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackeritmo.domain.entity.Category
import com.example.financetrackeritmo.domain.entity.Transaction
import com.example.financetrackeritmo.domain.usecase.AddTransactionUseCase
import com.example.financetrackeritmo.domain.usecase.GetAllCategoryUseCase
import com.example.financetrackeritmo.domain.usecase.UpdateTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val getAllCategoryUseCase: GetAllCategoryUseCase
) : ViewModel() {

    private val _validation = Channel<TransactionFieldsState>()
    val validateState = _validation.receiveAsFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories


    fun fetchCategories() {
        viewModelScope.launch {
            val categories = getAllCategoryUseCase().first()
            _categories.emit(categories)
        }
    }

    fun addNewTransaction(category: Category, amount: String, date: String, note: String) {

        if (validateTransaction(date, amount)) {

            viewModelScope.launch {
                val result = addTransactionUseCase(
                    Transaction(
                        categoryId = category.id,
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
        category: Category,
        date: String,
        amount: String,
        note: String
    ) {
        if (validateTransaction(date, amount)) {

            viewModelScope.launch {
                val result = updateTransactionUseCase(
                    Transaction(
                        id = id,
                        categoryId = category.id,
                        date = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        amount = amount.toDouble(),
                        note = note
                    )
                )
                if (result.isFailure) transactionError()
            }

        } else failedValidation(date, amount)

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

    private fun validateTransaction(date: String, amount: String): Boolean {
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