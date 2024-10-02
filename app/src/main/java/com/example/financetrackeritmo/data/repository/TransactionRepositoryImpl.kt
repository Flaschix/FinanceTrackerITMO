package com.example.financetrackeritmo.data.repository

import android.util.Log
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val mapper: TransactionMapper,
    private val transactionDao: TransactionDao
): TransactionRepository {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val refreshedListFlow = MutableSharedFlow<List<Transaction>>()

    private val refreshedSum = MutableSharedFlow<Unit>(replay = 1)

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
        started = SharingStarted.WhileSubscribed(5000),
        replay = 1
    )

    override suspend fun getAllTransaction(): SharedFlow<List<Transaction>> = transactions

    override suspend fun addTransaction(transaction: Transaction): Result<Unit> {
        val item = mapper.mapTransactionToTransactionDao(transaction)

        val generatedId = transactionDao.insertTransaction(item)

        _transactionList.add(transaction.copy(id = generatedId))
        refreshedListFlow.emit(transactionList)
        refreshedSum.emit(Unit)

        return Result.success(Unit)
    }

    override suspend fun deleteTransaction(transaction: Transaction): Result<Unit> {
        val item = mapper.mapTransactionToTransactionDao(transaction)

        transactionDao.deleteTransaction(item)

        _transactionList.removeIf { it.id == transaction.id }

        refreshedListFlow.emit(transactionList)
        refreshedSum.emit(Unit)

        return Result.success(Unit)
    }

    override suspend fun updateTransaction(transaction: Transaction): Result<Unit> {
        val item = mapper.mapTransactionToTransactionDao(transaction)

        transactionDao.updateTransaction(item)

        _transactionList.replaceAll { if (it.id == transaction.id) transaction else it }
        refreshedListFlow.emit(transactionList)
        refreshedSum.emit(Unit)

        return Result.success(Unit)
    }

    override suspend fun refreshTransactions(): Result<Unit> {
        coroutineScope.launch {
            val listDao = transactionDao.getAllTransactions()
            val result = mapper.mapListTransactionDaoToListTransaction(listDao)
            _transactionList.clear()
            _transactionList.addAll(result)
            refreshedListFlow.emit(transactionList)
            refreshedSum.emit(Unit)
        }
        return Result.success(Unit)
    }

    override suspend fun getIncome(): SharedFlow<Double> = flow {
        Log.d("TEST", "startIncome")

        refreshedSum.emit(Unit)

        refreshedSum.collect{
            val income = transactionDao.getIncome()
            Log.d("TEST", "getIncome: $income")
            emit(income)
        }
    }.shareIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5000),
        replay = 1
    )

    override suspend fun getExpense(): SharedFlow<Double> = flow {
        refreshedSum.emit(Unit)

        refreshedSum.collect{
            val income = transactionDao.getExpense()
            emit(income)
        }
    }.shareIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5000),
        replay = 1
    )
}