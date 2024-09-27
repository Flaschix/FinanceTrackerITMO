package com.example.financetrackeritmo.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@Entity(tableName = "transactions")
data class TransactionDB (
    @PrimaryKey(autoGenerate = true) val id: Long,
    val categoryName: String,
    val amount: Double,
    val date: LocalDate,
    val note: String
)

class ConvertersTransactionDate {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? {
        return date?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun toLocalDate(millisSinceEpoch: Long?): LocalDate? {
        return millisSinceEpoch?.let {
            LocalDate.ofEpochDay(millisSinceEpoch / (24 * 60 * 60 * 1000))
        }
    }
}