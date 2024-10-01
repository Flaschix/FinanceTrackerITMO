package com.example.financetrackeritmo.domain.repository

import com.example.financetrackeritmo.domain.entity.Transaction
import kotlinx.coroutines.flow.SharedFlow

interface TransactionRepository {

    suspend fun getAllTransaction(): SharedFlow<List<Transaction>>

    suspend fun addTransaction(transaction: Transaction): Result<Unit>

    suspend fun deleteTransaction(transaction: Transaction): Result<Unit>

    suspend fun updateTransaction(transaction: Transaction): Result<Unit>
}