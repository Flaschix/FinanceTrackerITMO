package com.example.financetrackeritmo.presentation.category

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
import androidx.navigation.fragment.findNavController
import com.example.financetrackeritmo.databinding.FragmentCategoryListBinding
import com.example.financetrackeritmo.domain.entity.Category
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryListFragment : Fragment() {

    private var _binding: FragmentCategoryListBinding? = null

    private val binding: FragmentCategoryListBinding
        get() = _binding ?: throw Exception("FragmentCategoryListBinding === null")

    private val viewModel by viewModels<CategoryListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{
                    when(it){
                        is CategoryListScreenState.Error -> {
                            Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                        }
                        CategoryListScreenState.Initial -> {}
                        CategoryListScreenState.Loading -> {
                            loadingStateView()
                        }
                        is CategoryListScreenState.Success -> {
                            successStateView()
                            setUpView(it.categories)
                        }
                    }
                }
            }
        }
    }

    private fun setUpView(data: List<Category>) {
        Log.d("TEST", "List: ${data}")

        binding.apply {
            btnNewTransaction.setOnClickListener{
                findNavController().navigate(CategoryListFragmentDirections.actionCategoryListFragmentToCategoryFragment(null))
            }
        }
    }

    private fun successStateView() {
        binding.pbCategory.visibility = View.GONE
    }

    private fun loadingStateView() {
        binding.pbCategory.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}