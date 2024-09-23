package com.example.financetrackeritmo.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class TransactionType : Parcelable {
    INCOME, EXPENSE
}