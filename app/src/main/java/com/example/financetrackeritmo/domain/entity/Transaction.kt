package com.example.financetrackeritmo.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Transaction (
    val id: Long = 0,
    val categoryId: Long,
    val amount: Double,
    val date: LocalDate,
    val note: String
) : Parcelable