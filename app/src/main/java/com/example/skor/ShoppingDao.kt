package com.example.skor

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {

    @Query("SELECT * FROM shopping_items ORDER BY id DESC")
    fun getAllItems(): Flow<List<ShoppingItem>>

    @Insert
    suspend fun insertItem(item: ShoppingItem)

    @Update
    suspend fun updateItem(item: ShoppingItem)

    @Delete
    suspend fun deleteItem(item: ShoppingItem)

    @Query("DELETE FROM shopping_items WHERE is_purchased = 1")
    suspend fun deleteCompletedItems()
}