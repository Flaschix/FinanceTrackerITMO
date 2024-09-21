package com.example.financetrackeritmo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.financetrackeritmo.data.dao.CategoryDao
import com.example.financetrackeritmo.data.entity.CategoryDB

//База данных. Сюда нужно будет добавить ещё транзакции (при каждом изменении бд, нужно менять его версию)
@Database(entities = [CategoryDB::class], version = 1)
abstract class TrackerDatabase: RoomDatabase() {

    abstract fun getCategoryDao(): CategoryDao
}