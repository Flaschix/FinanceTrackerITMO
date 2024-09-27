package com.example.financetrackeritmo.presentation.transaction.item

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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
import com.example.financetrackeritmo.domain.repository.CategoryRepository
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class TransactionFragment @Inject constructor(
) : Fragment() {

    private var _binding: FragmentTransactionBinding? = null

    private val binding: FragmentTransactionBinding
        get() = _binding ?: throw Exception("FragmentTransactionBinding === null")

    private val args by navArgs<TransactionFragmentArgs>()

    private lateinit var spinnerCategory: Spinner
    private lateinit var editTextDate: EditText
    private lateinit var btnConfirm: Button
    private lateinit var editAmount: TextInputEditText
    private lateinit var editNote: TextInputEditText

    private val viewModel by viewModels<TransactionViewModel>()

    private var categories: List<Category> = emptyList()

    @Inject
    lateinit var categoryRepository: CategoryRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(layoutInflater, container, false)

        spinnerCategory = binding.spinnerCategory
        editTextDate = binding.editTextDate
        editAmount = binding.editAmount
        editNote = binding.editNote
        btnConfirm = binding.btnConfirm

        lifecycleScope.launch {
            categoryRepository.getAllCategory().collect { fetchedCategories ->
                categories = fetchedCategories
                val categoryNames = fetchedCategories.map { it.name }
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    categoryNames
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCategory.adapter = adapter
            }
        }

        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }

        btnConfirm.setOnClickListener {
            onConfirmButtonClicked()
        }

        return binding.root
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
                editTextDate.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun onConfirmButtonClicked() {
        val selectedCategoryName = spinnerCategory.selectedItem.toString()
        val selectedCategory = categories.find { it.name == selectedCategoryName }
        val date = editTextDate.text.toString()
        val amountText = editAmount.text.toString()
        val note = editNote.text.toString()

        if (selectedCategory != null) {
            viewModel.saveTransaction(selectedCategory.name, date, amountText, note)

            // Navigate back or show a success message
            findNavController().navigateUp()
        } else {
            // Show an error message if the category is not found
            Toast.makeText(requireContext(), "Category not found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
    }

    private fun setUpView() {
        val transaction = args.transaction

        if (transaction == null) setUpAddMode()
        else setUpEditMode(transaction)

        observeValidation()
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

    private fun setUpEditMode(transaction: Transaction) {
        binding.apply {

            val categoryName = categories.find { it.name == transaction.categoryName }?.name
            if (categoryName != null) {
                val categoryIndex = categories.indexOfFirst { it.name == categoryName }
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
                        transaction.id, selectedCategory.name,
                        date, amountText, note
                    )

                    findNavController().navigateUp()
                } else {
                    Toast.makeText(requireContext(), "Category not found", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun setUpAddMode() {
        binding.apply {
            btnConfirm.setOnClickListener {
                onConfirmButtonClicked()
            }
        }
    }
}