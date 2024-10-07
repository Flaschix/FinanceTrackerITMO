package com.example.financetrackeritmo.presentation.transaction

import android.annotation.SuppressLint
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
        @SuppressLint("SetTextI18n")
        fun bind(transaction: Transaction) {
            binding.apply {
                viewModel.viewModelScope.launch {
                    categoryName.text = viewModel.getCategoryNameById(transaction.categoryId)
                    val parts = transaction.date.toString().split("-")
                    val year = parts[0].takeLast(2)
                    val month = parts[1]
                    val day = parts[2]
                    transactionDate.text = "$day/$month/$year"
                    transactionAmount.text = transaction.amount.toString() + " ₽"
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