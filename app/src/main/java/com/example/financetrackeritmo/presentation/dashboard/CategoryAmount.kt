package com.example.financetrackeritmo.presentation.dashboard

import androidx.recyclerview.widget.DiffUtil

data class CategoryAmount(
    val name: String,
    val percentage: Double,
    val amount: Double
)

class CategoryAmountDiffCallback : DiffUtil.ItemCallback<CategoryAmount>() {
    override fun areItemsTheSame(oldItem: CategoryAmount, newItem: CategoryAmount): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: CategoryAmount, newItem: CategoryAmount): Boolean {
        return oldItem == newItem
    }
}
