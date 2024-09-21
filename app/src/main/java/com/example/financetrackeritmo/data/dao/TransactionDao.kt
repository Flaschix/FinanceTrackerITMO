package com.example.financetrackeritmo.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.financetrackeritmo.data.entity.TransactionDB

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): List<TransactionDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionDB)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionDB)

    @Query("SELECT * FROM transactions WHERE id=:transactionId")
    fun getTransactionById(transactionId: Long): TransactionDB?
}