package com.example.financetrackeritmo.domain.entity

data class Category(
    val id: Long = 0,
    val name: String,
    val type: TransactionType
)