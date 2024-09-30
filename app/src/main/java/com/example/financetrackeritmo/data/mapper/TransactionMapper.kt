package com.example.financetrackeritmo.data.mapper

import com.example.financetrackeritmo.data.entity.TransactionDB
import com.example.financetrackeritmo.domain.entity.Transaction
import javax.inject.Inject

class TransactionMapper @Inject constructor() {

    fun mapTransactionDaoToTransaction (transactionEntity: TransactionDB): Transaction {
        return Transaction (
            id = transactionEntity.transactionId,
            category = transactionEntity.category,
            amount = transactionEntity.amount,
            date = transactionEntity.date,
            note = transactionEntity.note
        )
    }

    fun mapListTransactionDaoToListTransaction(listDao: List<TransactionDB>): List<Transaction>{
        return listDao.map { mapTransactionDaoToTransaction(it) }
    }

    fun mapTransactionToTransactionDao(transaction: Transaction): TransactionDB {
        return TransactionDB (
            transactionId = transaction.id,
            category = transaction.category,
            amount = transaction.amount,
            date = transaction.date,
            note = transaction.note
        )
    }
}