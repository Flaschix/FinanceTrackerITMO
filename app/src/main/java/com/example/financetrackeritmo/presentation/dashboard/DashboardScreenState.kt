package com.example.financetrackeritmo.presentation.dashboard

import com.example.financetrackeritmo.domain.entity.Category
import com.example.financetrackeritmo.domain.entity.Transaction

sealed class DashboardScreenState {
    data object Initial: DashboardScreenState()

    data object Loading: DashboardScreenState()

    data class Success(
        val categories: List<Category>,
        val transactions: List<Transaction>
    ): DashboardScreenState()

    data class Error(val msg: String): DashboardScreenState()
}