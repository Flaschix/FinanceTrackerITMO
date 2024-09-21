package com.example.financetrackeritmo.data.repository

import com.example.financetrackeritmo.data.dao.CategoryDao
import com.example.financetrackeritmo.data.mapper.CategoryMapper
import com.example.financetrackeritmo.domain.entity.Category
import com.example.financetrackeritmo.domain.repository.CategoryRepository
import com.example.financetrackeritmo.utils.mergeWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
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

    private val categories: StateFlow<List<Category>> = flow {
        val listDao = categoryDao.getAllCategories()
        val result = mapper.mapListCategoryDaoToListCategory(listDao)
        _categoryList.addAll(result)

        emit(categoryList)
    }.mergeWith(refreshedListFlow).stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

    override suspend fun getAllCategory(): StateFlow<List<Category>> = categories

    override suspend fun addCategory(category: Category) {
        val item = mapper.mapCategoryToCategoryDao(category)

        categoryDao.insertCategory(item)

        _categoryList.add(category)
        refreshedListFlow.emit(categoryList)
    }

    override suspend fun deleteCategory(category: Category) {
        val item = mapper.mapCategoryToCategoryDao(category)

        categoryDao.deleteCategory(item)
    }

    override suspend fun updateCategory(category: Category) {
        val item = mapper.mapCategoryToCategoryDao(category)

        categoryDao.insertCategory(item)

        _categoryList.map { if(it.id == category.id) it.copy(name = category.name, type = category.type) else it }
        refreshedListFlow.emit(categoryList)
    }
}