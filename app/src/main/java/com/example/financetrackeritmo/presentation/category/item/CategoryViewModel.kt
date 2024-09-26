package com.example.financetrackeritmo.presentation.category.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackeritmo.domain.entity.Category
import com.example.financetrackeritmo.domain.entity.TransactionType
import com.example.financetrackeritmo.domain.usecase.AddCategoryUseCase
import com.example.financetrackeritmo.domain.usecase.GetAllCategoryUseCase
import com.example.financetrackeritmo.domain.usecase.UpdateCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
): ViewModel() {

    private val _validation = Channel<CategoryFieldsState>()
    val validateState = _validation.receiveAsFlow()

    fun addNewCategory(name: String, type: TransactionType){
        if(validateFields(name)){

            viewModelScope.launch {
                val result = addCategoryUseCase(Category(name = name, type = type))
                if (result.isFailure) nameExisted()

            }

        } else failedValidation(name)
    }

    fun editCategory(id: Long, name: String, type: TransactionType){
        if (validateFields(name)){

            viewModelScope.launch {
                val result = updateCategoryUseCase(Category(id = id, name = name, type = type))
                if (result.isFailure) nameExisted()
            }

        } else failedValidation(name)

    }

    private fun validateName(name: String): CategoryValidateState{
        if(name.isEmpty()) return CategoryValidateState.Error("Fill this field")
        if(name.startsWith(" ") || name.endsWith(" ")) return CategoryValidateState
            .Error("the title cannot contain a space at the beginning or at the end")

        return CategoryValidateState.Success
    }

    private fun validateFields(name: String): Boolean{
        val nameV = validateName(name)

        return nameV is CategoryValidateState.Success
    }

    private fun failedValidation(name: String){
        val fieldsState = CategoryFieldsState(
            validateName(name)
        )

        viewModelScope.launch {
            _validation.send(fieldsState)
        }
    }

    private fun nameExisted(){
        viewModelScope.launch {
            _validation.send(
                CategoryFieldsState(
                    CategoryValidateState.Error("This category already exists")
                )
            )
        }
    }

}