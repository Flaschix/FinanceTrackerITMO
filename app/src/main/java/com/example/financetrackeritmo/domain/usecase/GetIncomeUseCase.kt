package com.example.financetrackeritmo.domain.usecase

import com.example.financetrackeritmo.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class GetIncomeUseCase @Inject constructor(private val repository: TransactionRepository){
    suspend operator fun invoke(): SharedFlow<Double> {
        return repository.getIncome()
    }
}