package com.example.financetrackeritmo.presentation.category.item

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.financetrackeritmo.databinding.FragmentCategoryBinding
import com.example.financetrackeritmo.domain.entity.Category
import com.example.financetrackeritmo.domain.entity.TransactionType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null

    private val binding: FragmentCategoryBinding
        get() = _binding ?: throw Exception("FragmentCategoryBinding === null")

    private val args by navArgs<CategoryFragmentArgs>()

    private val viewModel by viewModels<CategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
    }

    private fun setUpView() {
        val category = args.category

        if(category == null) setUpAddMode()
        else setUpEditMode(category)

        observeValidation()
    }

    private fun observeValidation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.validateState.collect {
                    if (it.name is CategoryValidateState.Error) {
                        binding.tiedName.apply {
                            requestFocus()
                            error = it.name.msg
                        }
                    }
                }
            }
        }
    }

    private fun setUpEditMode(category: Category) {
        binding.apply {
            when(category.type){
                TransactionType.INCOME -> switchType.isChecked = true
                TransactionType.EXPENSE -> switchType.isChecked = false
            }

            tiedName.setText(category.name)

            btnConfirm.setOnClickListener {
                val name = tiedName.text.toString()
                val type = if(switchType.isChecked) TransactionType.INCOME else TransactionType.EXPENSE

                viewModel.editCategory(category.id, name, type)
            }
        }
    }

    private fun setUpAddMode() {
        binding.apply {

            btnConfirm.setOnClickListener {
                val name = tiedName.text.toString()
                val type = if(switchType.isChecked) TransactionType.INCOME else TransactionType.EXPENSE

                viewModel.addNewCategory(name, type)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}