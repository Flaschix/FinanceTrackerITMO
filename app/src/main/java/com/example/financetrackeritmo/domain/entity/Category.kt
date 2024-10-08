package com.example.financetrackeritmo.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: Long = 0,
    val name: String,
    val type: TransactionType
) : Parcelable {
    override fun toString(): String {
        return name
    }
}