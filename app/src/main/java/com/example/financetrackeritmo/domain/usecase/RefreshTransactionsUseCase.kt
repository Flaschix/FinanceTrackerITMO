package com.example.financetrackeritmo.domain.usecase

import com.example.financetrackeritmo.domain.entity.Transaction
import com.example.financetrackeritmo.domain.repository.TransactionRepository
import javax.inject.Inject

class RefreshTransactionsUseCase @Inject constructor(private val repository: TransactionRepository){
    suspend operator fun invoke(): Result<Unit> {
        return repository.refreshTransactions()
    }
}