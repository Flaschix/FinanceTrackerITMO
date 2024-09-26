package com.example.financetrackeritmo.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

@Entity(tableName = "transactions")
data class TransactionDB (
    @PrimaryKey(autoGenerate = true) val id: Long,
    val categoryId: Long,
    val amount: Double,
    val date: Date,
    val note: String
)

class ConvertersTransactionDate {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}