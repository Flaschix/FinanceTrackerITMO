package com.example.financetrackeritmo.presentation.dashboard

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
        fun bind(categoryAmount: CategoryAmount){
            binding.tvCategoryName.text = categoryAmount.name
            binding.tvCategoryAmount.text = categoryAmount.amount.toString()
            binding.tvCategoryPercentage.text = categoryAmount.percentage.toString()
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