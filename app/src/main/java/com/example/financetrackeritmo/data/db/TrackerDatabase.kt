package com.example.financetrackeritmo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.financetrackeritmo.data.dao.CategoryDao
import com.example.financetrackeritmo.data.entity.CategoryDB
import com.example.financetrackeritmo.data.dao.TransactionDao
import com.example.financetrackeritmo.data.entity.TransactionDB

//База данных. Сюда нужно будет добавить ещё транзакции (при каждом изменении бд, нужно менять его версию)
@Database(entities = [CategoryDB::class, TransactionDB::class], version = 1)
abstract class TrackerDatabase: RoomDatabase() {

    abstract fun getCategoryDao(): CategoryDao
    abstract fun getTransactionDao(): TransactionDao
}