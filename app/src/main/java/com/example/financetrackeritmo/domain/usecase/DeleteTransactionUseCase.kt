package com.example.financetrackeritmo.domain.usecase

import com.example.financetrackeritmo.domain.entity.Transaction
import com.example.financetrackeritmo.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(private val repository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction): Result<Unit> {
        return repository.deleteTransaction(transaction)
    }
}