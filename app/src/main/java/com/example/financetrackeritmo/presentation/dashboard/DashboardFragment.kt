package com.example.financetrackeritmo.presentation.dashboard

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.financetrackeritmo.databinding.FragmentDashboardBinding
import com.example.financetrackeritmo.domain.entity.Category
import com.example.financetrackeritmo.domain.entity.Transaction
import com.example.financetrackeritmo.domain.entity.TransactionType
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val binding: FragmentDashboardBinding
        get() = _binding ?: throw Exception("FragmentDashboardBinding === null")

    private val viewModel by viewModels<DashboardViewModel>()

    private val dashboardAdapter by lazy {
        DashboardAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{

                    when(it){
                        is DashboardScreenState.Error -> {
                            Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                        }
                        DashboardScreenState.Initial -> {}
                        DashboardScreenState.Loading -> {
                            loadingStateView()
                        }
                        is DashboardScreenState.Success -> {
                            successStateView()

                            setUpRV()

                            fillRV(it.totalIncome, it.totalExpense, it.categories, it.transactions, it.isIncomeMode)

                            setUpView(it.totalIncome, it.totalExpense, it.isIncomeMode)

                            setUpPieChart(it.totalIncome, it.totalExpense, it.categories, it.transactions, it.isIncomeMode)
                        }
                    }

                }
            }
        }
    }

    private fun setUpView(
        totalIncome: Double,
        totalExpense: Double,
        mode: Boolean
    ) {
        binding.switchMode.isChecked = mode

        binding.switchMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchMode()
        }

        binding.tvTotalAmount.text = if (mode) {
            "Total Income: ${totalIncome}"
        } else {
            "Total Expense: ${totalExpense}"
        }
    }

    private fun fillRV(
        totalIncome: Double,
        totalExpense: Double,
        categoryList: List<Category>,
        transactionList: List<Transaction>,
        mode: Boolean
    ) {

        val filteredCategories = categoryList.filter {
            it.type == if (mode) TransactionType.INCOME else TransactionType.EXPENSE
        }

        val categoryAmounts = filteredCategories.map { category ->
            val totalAmount = transactionList.filter { it.categoryId == category.id }
                .sumOf { it.amount }
            val percentage = if (mode) {
                (totalAmount / totalIncome) * 100
            } else {
                (totalAmount / totalExpense) * 100
            }
            CategoryAmount(category.name, percentage, totalAmount)
        }

        dashboardAdapter.submitList(categoryAmounts)
    }

    private fun setUpPieChart(
        totalIncome: Double,
        totalExpense: Double,
        categoryList: List<Category>,
        transactionList: List<Transaction>,
        mode: Boolean
    ) {
        val filteredCategories = categoryList.filter {
            it.type == if (mode) TransactionType.INCOME else TransactionType.EXPENSE
        }

        val pieEntries = filteredCategories.map { category ->
            val totalAmount = transactionList.filter { it.categoryId == category.id }
                .sumOf { it.amount }
            PieEntry(totalAmount.toFloat(), category.name)
        }

        val pieDataSet = PieDataSet(pieEntries, "Categories")
        pieDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 12f

        val pieData = PieData(pieDataSet)

        binding.pieChart.data = pieData
        binding.pieChart.description.isEnabled = false
        binding.pieChart.isDrawHoleEnabled = false
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.invalidate() // refresh
    }

    private fun setUpRV() {
        binding.rvDashboard.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = dashboardAdapter
        }
    }

    private fun successStateView() {
        binding.apply {
            pbDashboard.visibility = View.GONE
            tvTotalAmount.visibility = View.VISIBLE
            switchMode.visibility = View.VISIBLE
            pieChart.visibility = View.VISIBLE
            rvDashboard.visibility = View.VISIBLE
        }
    }

    private fun loadingStateView() {
        binding.apply {
            pbDashboard.visibility = View.VISIBLE
            tvTotalAmount.visibility = View.GONE
            switchMode.visibility = View.GONE
            pieChart.visibility = View.GONE
            rvDashboard.visibility = View.GONE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}