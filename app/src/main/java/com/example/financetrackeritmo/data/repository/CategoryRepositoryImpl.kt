package com.example.financetrackeritmo.data.repository

import com.example.financetrackeritmo.data.dao.CategoryDao
import com.example.financetrackeritmo.data.dao.TransactionDao
import com.example.financetrackeritmo.data.entity.CategoryDB
import com.example.financetrackeritmo.data.mapper.CategoryMapper
import com.example.financetrackeritmo.domain.entity.Category
import com.example.financetrackeritmo.domain.repository.CategoryRepository
import com.example.financetrackeritmo.domain.repository.TransactionRepository
import com.example.financetrackeritmo.utils.mergeWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val mapper: CategoryMapper,
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao,
    private val transactionRepository: TransactionRepository
): CategoryRepository {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val refreshedListFlow = MutableSharedFlow<List<Category>>()

    private val _categoryList = mutableListOf<Category>()

    private val categoryList: List<Category>
        get() = _categoryList.toList()

    private val categories: SharedFlow<List<Category>> = flow {
        val listDao = categoryDao.getAllCategories()
        val result = mapper.mapListCategoryDaoToListCategory(listDao)
        _categoryList.addAll(result)

        delay(3000)

        emit(categoryList)
    }.mergeWith(refreshedListFlow).shareIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5000),
        replay = 1
    )

    override suspend fun getAllCategory(): SharedFlow<List<Category>> = categories

    override suspend fun addCategory(category: Category): Result<Unit> {
        val itemFromBd = categoryDao.getCategoryByName(category.name)

        if(itemFromBd != null) return Result.failure(Exception("Category with name ${category.name} already exists"))

        val item = mapper.mapCategoryToCategoryDao(category)

        val generatedId = categoryDao.insertCategory(item)

        _categoryList.add(category.copy(id = generatedId))
        refreshedListFlow.emit(categoryList)

        return Result.success(Unit)
    }

    override suspend fun deleteCategory(category: Category): Result<Unit> {
        val item = mapper.mapCategoryToCategoryDao(category)

        try {
            transactionDao.deleteTransactionsByCategory(category.id)
            categoryDao.deleteCategory(item)
        } catch (e: Exception){
            return Result.failure(Exception("Failed to delete"))
        }

        _categoryList.removeIf { it.id == category.id }

        refreshedListFlow.emit(categoryList)

        transactionRepository.refreshTransactions()

        return Result.success(Unit)
    }

    override suspend fun updateCategory(category: Category): Result<Unit> {
        val itemFromBd = categoryDao.getCategoryByName(category.name)

        if(itemFromBd != null && itemFromBd.id != category.id) return Result.failure(Exception("Category with name ${category.name} already exists"))

        val item = mapper.mapCategoryToCategoryDao(category)

        categoryDao.updateCategory(item)

        _categoryList.replaceAll { if (it.id == category.id) category else it }
        refreshedListFlow.emit(categoryList)

        transactionRepository.refreshTransactions()

        return Result.success(Unit)
    }

    override suspend fun getCategoryNameById(categoryId: Long): String {
        val itemFromBd = categoryDao.getCategoryById(categoryId)
        return if (itemFromBd != null) {
            val item = mapper.mapCategoryDaoToCategory(itemFromBd)
            item.name
        } else {
            "Unknown Category"
        }
    }
}