package com.example.financetrackeritmo.presentation.dashboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.financetrackeritmo.databinding.FragmentDashboardBinding
import com.example.financetrackeritmo.databinding.RvDashboardBinding

class DashboardAdapter: ListAdapter<CategoryAmount, DashboardAdapter.DashboardViewHolder>(
    CategoryAmountDiffCallback()
) {

    inner class DashboardViewHolder(val binding: RvDashboardBinding): ViewHolder(binding.root){
        @SuppressLint("DefaultLocale", "SetTextI18n")
        fun bind(categoryAmount: CategoryAmount){
            binding.apply {
                tvCategoryName.text = categoryAmount.name
                tvCategoryAmount.text = categoryAmount.amount.toString() + " ₽"
                tvCategoryPercentage.text = if(categoryAmount.percentage > 0)
                    String.format("%.2f", categoryAmount.percentage) + "%" else "0%"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        return DashboardViewHolder(
            RvDashboardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val categoryAmount = getItem(position)

        holder.bind(categoryAmount)
    }
}