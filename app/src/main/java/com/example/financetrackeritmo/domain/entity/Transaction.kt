package com.example.financetrackeritmo.domain.entity

import java.time.LocalDate

data class Transaction (
    val id: Long = 0,
    val categoryId: Long,
    val amount: Double,
    val date: LocalDate,
    val note: String
)