package com.example.financetrackeritmo.data.repository

import com.example.financetrackeritmo.data.dao.TransactionDao
import com.example.financetrackeritmo.data.mapper.TransactionMapper
import com.example.financetrackeritmo.domain.entity.Transaction
import com.example.financetrackeritmo.domain.repository.TransactionRepository
import com.example.financetrackeritmo.utils.mergeWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val mapper: TransactionMapper,
    private val transactionDao: TransactionDao
): TransactionRepository {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val refreshedListFlow = MutableSharedFlow<List<Transaction>>()

    private val _transactionList = mutableListOf<Transaction>()

    private val transactionList: List<Transaction>
        get() = _transactionList.toList()

    private val transactions: SharedFlow<List<Transaction>> = flow {
        val listDao = transactionDao.getAllTransactions()
        val result = mapper.mapListTransactionDaoToListTransaction(listDao)
        _transactionList.addAll(result)

        delay(3000)

        emit(transactionList)
    }.mergeWith(refreshedListFlow).shareIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily
    )

    override suspend fun getAllTransaction(): SharedFlow<List<Transaction>> = transactions

    override suspend fun addTransaction(transaction: Transaction): Result<Unit> {
        val item = mapper.mapTransactionToTransactionDao(transaction)

        val generatedId = transactionDao.insertTransaction(item)

        _transactionList.add(transaction.copy(id = generatedId))
        refreshedListFlow.emit(transactionList)

        return Result.success(Unit)
    }

    override suspend fun deleteTransaction(transaction: Transaction): Result<Unit> {
        val item = mapper.mapTransactionToTransactionDao(transaction)

        transactionDao.deleteTransaction(item)

        val index = transactionList.indexOfFirst { it.id == item.id }
        _transactionList.drop(index)

        return Result.success(Unit)
    }

    override suspend fun updateTransaction(transaction: Transaction): Result<Unit> {
        val item = mapper.mapTransactionToTransactionDao(transaction)

        transactionDao.updateTransaction(item)

        _transactionList.replaceAll { if (it.id == transaction.id) transaction else it }
        refreshedListFlow.emit(transactionList)

        return Result.success(Unit)
    }
}