package com.example.skor

import kotlinx.coroutines.flow.Flow

class ShoppingRepository(private val shoppingDao: ShoppingDao) {

    val allItems: Flow<List<ShoppingItem>> = shoppingDao.getAllItems()

    suspend fun insert(item: ShoppingItem) = shoppingDao.insertItem(item)

    suspend fun update(item: ShoppingItem) = shoppingDao.updateItem(item)

    suspend fun delete(item: ShoppingItem) = shoppingDao.deleteItem(item)

    suspend fun deleteCompleted() = shoppingDao.deleteCompletedItems()
}