package com.example.financetrackeritmo.domain.repository

import com.example.financetrackeritmo.domain.entity.Transaction
import kotlinx.coroutines.flow.SharedFlow

interface TransactionRepository {

    suspend fun getAllTransaction(): SharedFlow<List<Transaction>>

    suspend fun addTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)
}