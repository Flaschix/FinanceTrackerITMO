package com.example.financetrackeritmo.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryDB (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String
)