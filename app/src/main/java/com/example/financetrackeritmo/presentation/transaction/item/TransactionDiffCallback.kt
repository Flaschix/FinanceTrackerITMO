package com.example.financetrackeritmo.presentation.transaction.item

import androidx.recyclerview.widget.DiffUtil
import com.example.financetrackeritmo.domain.entity.Transaction

class TransactionDiffCallback: DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }
}