package com.example.financetrackeritmo.presentation.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.financetrackeritmo.databinding.RvCategoryListBinding
import com.example.financetrackeritmo.domain.entity.Category
import com.example.financetrackeritmo.domain.entity.TransactionType
import com.example.financetrackeritmo.presentation.category.item.CategoryDiffCallback


class CategoryListAdapter: ListAdapter<Category, CategoryListAdapter.CategoryListViewHolder>(
    CategoryDiffCallback()
) {
    var deleteClickListener: ((Category) -> Unit)? = null

    var editClickListener: ((Category) -> Unit)? = null

    inner class CategoryListViewHolder(val binding: RvCategoryListBinding): ViewHolder(binding.root){
        fun bind(category: Category){
            binding.apply {
                tvName.text = category.name
                tvType.text = if (category.type == TransactionType.INCOME) "Доходы" else "Расходы"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        return CategoryListViewHolder(
            RvCategoryListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
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