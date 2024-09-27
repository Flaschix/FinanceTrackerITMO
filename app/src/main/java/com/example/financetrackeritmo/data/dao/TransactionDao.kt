package com.example.financetrackeritmo.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.financetrackeritmo.data.entity.TransactionDB

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): List<TransactionDB>

    @Insert
    suspend fun insertTransaction(transaction: TransactionDB) : Long

    @Delete
    suspend fun deleteTransaction(transaction: TransactionDB)

    @Update
    suspend fun updateTransaction(transaction: TransactionDB)

    @Query("SELECT * FROM transactions WHERE id=:transactionId")
    suspend fun getTransactionById(transactionId: Long): TransactionDB?
}