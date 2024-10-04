package com.example.financetrackeritmo.presentation.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.financetrackeritmo.databinding.RvTransactionListBinding
import com.example.financetrackeritmo.domain.entity.Transaction
import com.example.financetrackeritmo.presentation.transaction.item.TransactionDiffCallback
import kotlinx.coroutines.launch

class TransactionListAdapter(
    private val viewModel: TransactionListViewModel
) : ListAdapter<Transaction, TransactionListAdapter.TransactionListViewHolder>(
    TransactionDiffCallback(),
) {
    var deleteClickListener: ((Transaction) -> Unit)? = null

    var editClickListener: ((Transaction) -> Unit)? = null


    inner class TransactionListViewHolder(
        val binding: RvTransactionListBinding,
    ) : ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            binding.apply {
                viewModel.viewModelScope.launch {
                    categoryName.text = viewModel.getCategoryNameById(transaction.categoryId)
                    transactionDate.text = transaction.date.toString()
                    transactionAmount.text = transaction.amount.toString()
                }
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
        val transaction = getItem(position)

        holder.bind(transaction)

        holder.binding.imgEdit.setOnClickListener {
            editClickListener?.invoke(transaction)
        }

        holder.binding.imgDelete.setOnClickListener {
            deleteClickListener?.invoke(transaction)
        }
    }
}