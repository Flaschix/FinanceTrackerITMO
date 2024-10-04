package com.example.financetrackeritmo.presentation.transaction.item

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.financetrackeritmo.databinding.FragmentTransactionBinding
import com.example.financetrackeritmo.domain.entity.Category
import com.example.financetrackeritmo.domain.entity.Transaction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class TransactionFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding: FragmentTransactionBinding
        get() = _binding ?: throw Exception("FragmentTransactionBinding === null")

    private val args by navArgs<TransactionFragmentArgs>()
    private val viewModel by viewModels<TransactionViewModel>()

    private var categories: List<Category> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextDate.setOnClickListener {
            showDatePickerDialog()
        }

        viewModel.fetchCategories()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collect { list ->
                    Log.d("DB", "Got categories $list")
                    updateSpinner(list)
                    categories = list
                }
            }
        }

        setUpView()
    }

    private fun updateSpinner(categories: List<Category>) {
        if (binding.spinnerCategory.selectedItem == null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categories.map { it.name })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCategory.adapter = adapter
        } 
    }

    private fun setUpView() {
        val transaction = args.transaction

        if (transaction == null) setUpAddMode()
        else setUpEditMode(transaction)

        observeValidation()

        observeOperationSuccess()
    }

    private fun setUpAddMode() {
        binding.apply {
            btnConfirm.setOnClickListener {
                Log.d("TEST", "categories: $categories")

                val category =
                    categories.find { spinnerCategory.selectedItem.toString() == it.name }
                val amount = editAmount.text.toString()
                val dateString = editTextDate.text.toString()
                val note = editNote.text.toString()

                Log.d("TEST", "category: $category")

                if (category != null) viewModel.addNewTransaction(
                    category,
                    amount,
                    dateString,
                    note
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeValidation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.validateState.collect { fieldsState ->
                    if (fieldsState.amount is TransactionValidateState.Error) {
                        binding.editAmount.apply {
                            requestFocus()
                            error = fieldsState.amount.msg
                        }
                    }

                    if (fieldsState.date is TransactionValidateState.Error) {
                        binding.editTextDate.apply {
                            requestFocus()
                            error = fieldsState.date.msg
                        }
                    }
                }
            }
        }
    }

    private fun showDatePickerDialog() { // TODO use DatePicker
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                binding.editTextDate.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun observeOperationSuccess() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.operationSuccess.collect { success ->
                    if (success) {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun setUpEditMode(transaction: Transaction) {
        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.categories.collect { list ->
                    categories = list
                }
            }
            updateSpinner(categories)
            val categoryId = transaction.categoryId
            val categoryIndex = categories.indexOfFirst { it.id == categoryId }
            if (categoryIndex != -1) {
                spinnerCategory.setSelection(categoryIndex)
            }

            editTextDate.setText(transaction.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            editAmount.setText(transaction.amount.toString())
            editNote.setText(transaction.note)

            btnConfirm.setOnClickListener {
                val selectedCategoryName = spinnerCategory.selectedItem.toString()
                val selectedCategory = categories.find { it.name == selectedCategoryName }
                val date = editTextDate.text.toString()
                val amountText = editAmount.text.toString()
                val note = editNote.text.toString()

                if (selectedCategory != null) {
                    viewModel.editTransaction(
                        transaction.id, selectedCategory,
                        date, amountText, note
                    )
                } else {
                    Toast.makeText(requireContext(), "Category not found", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}