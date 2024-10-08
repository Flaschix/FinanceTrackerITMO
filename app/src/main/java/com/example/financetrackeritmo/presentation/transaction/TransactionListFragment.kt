package com.example.financetrackeritmo.presentation.transaction

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.financetrackeritmo.databinding.FragmentTransactionListBinding
import com.example.financetrackeritmo.domain.entity.Transaction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransactionListFragment : Fragment() {

    private var _binding: FragmentTransactionListBinding? = null

    private val binding: FragmentTransactionListBinding
        get() = _binding ?: throw Exception("FragmentTransactionListBinding === null")

    private val viewModel by viewModels<TransactionListViewModel>()

    private val transactionListAdapter by lazy {
        TransactionListAdapter(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is TransactionListScreenState.Error -> {
                            Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                        }

                        TransactionListScreenState.Initial -> {}
                        TransactionListScreenState.Loading -> {
                            loadingStateView()
                        }

                        is TransactionListScreenState.Success -> {
                            successStateView()
                            setUpView(it.transactions)
                            setUpRV()
                            transactionListAdapter.submitList(it.transactions)
                        }
                    }
                }
            }
        }
    }

    private fun setUpRV() {
        binding.rvTransactionList.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = transactionListAdapter
        }

        transactionListAdapter.editClickListener = {
            findNavController().navigate(
                TransactionListFragmentDirections.actionTransactionListFragmentToTransactionFragment(
                    it
                )
            )
        }

        transactionListAdapter.deleteClickListener = {
            viewModel.deleteTransaction(it)
        }
    }

    private fun setUpView(data: List<Transaction>) {
        Log.d("TEST", "List: ${data}")

        binding.apply {
            btnAddTransaction.setOnClickListener {
                findNavController().navigate(
                    TransactionListFragmentDirections.actionTransactionListFragmentToTransactionFragment(
                        null
                    )
                )
            }
        }
    }

    private fun successStateView() {
        binding.pbTransaction.visibility = View.GONE
    }

    private fun loadingStateView() {
        binding.pbTransaction.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}