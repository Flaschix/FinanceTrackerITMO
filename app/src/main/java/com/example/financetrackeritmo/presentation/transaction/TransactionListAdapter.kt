package com.example.financetrackeritmo.presentation.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.financetrackeritmo.databinding.RvTransactionListBinding
import com.example.financetrackeritmo.domain.entity.Transaction
import com.example.financetrackeritmo.presentation.transaction.item.TransactionDiffCallback

class TransactionListAdapter : ListAdapter<Transaction, TransactionListAdapter.TransactionListViewHolder>(
    TransactionDiffCallback()
) {
    var deleteClickListener: ((Transaction) -> Unit)? = null

    var editClickListener: ((Transaction) -> Unit)? = null


    inner class TransactionListViewHolder(val binding: RvTransactionListBinding): ViewHolder(binding.root){
        fun bind(transaction: Transaction){
            binding.apply {
                categoryName.text = transaction.category.name
                transactionDate.text = transaction.date.toString()
                transactionAmount.text = transaction.amount.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionListViewHolder {
        return TransactionListViewHolder(
            RvTransactionListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TransactionListViewHolder, position: Int) {
        val category = getItem(position)

        holder.bind(category)

        holder.binding.imgEdit.setOnClickListener {
            editClickListener?.invoke(category)
        }

        holder.binding.imgDelete.setOnClickListener {
            deleteClickListener?.invoke(category)
        }
    }
}