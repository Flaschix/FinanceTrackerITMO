package com.example.financetrackeritmo.data.mapper

import com.example.financetrackeritmo.data.entity.CategoryDB
import com.example.financetrackeritmo.domain.entity.Category
import com.example.financetrackeritmo.domain.entity.TransactionType
import javax.inject.Inject

class CategoryMapper @Inject constructor() {

    fun mapCategoryDaoToCategory(categoryEntity: CategoryDB): Category {
        return Category(
            id = categoryEntity.id,
            type = TransactionType.valueOf(categoryEntity.type),
            name = categoryEntity.name,
        )
    }

    fun mapListCategoryDaoToListCategory(listDao: List<CategoryDB>): List<Category>{
        return listDao.map { mapCategoryDaoToCategory(it) }
    }

    fun mapCategoryToCategoryDao(category: Category): CategoryDB{
        return CategoryDB(
            id = category.id,
            name = category.name,
            type = category.type.name
        )
    }
}