package com.example.financetrackeritmo.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
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

    @Query("SELECT * FROM transactions WHERE transactionId=:transactionId")
    suspend fun getTransactionById(transactionId: Long): TransactionDB?

    @Query("DELETE FROM transactions WHERE categoryid = :categoryId")
    suspend fun deleteTransactionsByCategory(categoryId: Long)

    @Query("""
        SELECT 
            COALESCE(SUM(t.amount), 0) 
        FROM transactions t
        JOIN categories c ON t.categoryId = c.id
        WHERE c.type = 'INCOME'
    """)
    suspend fun getIncome(): Double

    @Query("""
        SELECT 
            COALESCE(SUM(t.amount), 0) 
        FROM transactions t
        JOIN categories c ON t.categoryId = c.id
        WHERE c.type = 'EXPENSE'
    """)
    suspend fun getExpense(): Double
}