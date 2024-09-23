package com.example.financetrackeritmo.data.repository

import android.util.Log
import com.example.financetrackeritmo.data.dao.CategoryDao
import com.example.financetrackeritmo.data.mapper.CategoryMapper
import com.example.financetrackeritmo.domain.entity.Category
import com.example.financetrackeritmo.domain.repository.CategoryRepository
import com.example.financetrackeritmo.utils.mergeWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val mapper: CategoryMapper,
    private val categoryDao: CategoryDao,
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
        started = SharingStarted.Lazily,
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

        categoryDao.deleteCategory(item)

        _categoryList.removeIf { it.id == category.id }

        refreshedListFlow.emit(categoryList)

        return Result.success(Unit)
    }

    override suspend fun updateCategory(category: Category): Result<Unit> {
        val itemFromBd = categoryDao.getCategoryByName(category.name)

        if(itemFromBd != null && itemFromBd.id != category.id) return Result.failure(Exception("Category with name ${category.name} already exists"))

        val item = mapper.mapCategoryToCategoryDao(category)

        Log.d("TEST", "category $category")
        Log.d("TEST", "item $item")

        categoryDao.updateCategory(item)

        _categoryList.replaceAll { if (it.id == category.id) category else it }
        refreshedListFlow.emit(categoryList)

        return Result.success(Unit)
    }
}