package com.example.financetrackeritmo.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.financetrackeritmo.data.entity.CategoryDB

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAllCategories(): List<CategoryDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryDB): Long

    @Delete
    suspend fun deleteCategory(category: CategoryDB)

    @Update
    suspend fun updateCategory(category: CategoryDB)

    @Query("SELECT * FROM categories WHERE name=:categoryName")
    suspend fun getCategoryByName(categoryName: String): CategoryDB?
}