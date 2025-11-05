package com.example.skor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skor.ShoppingItem
import com.example.skor.ShoppingRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.launch

class ShoppingListViewModel(private val repository: ShoppingRepository) : ViewModel() {

    //val shoppingItems = repository.allItems
    // Преобразуем Flow в LiveData
    val shoppingItems: LiveData<List<ShoppingItem>> = repository.allItems.asLiveData()
    fun addNewItem(name: String, quantity: Int) {
        val newItem = ShoppingItem(name = name, quantity = quantity)
        viewModelScope.launch {
            repository.insert(newItem)
        }
    }

    fun updateItem(item: ShoppingItem) {
        viewModelScope.launch {
            repository.update(item)
        }
    }

    fun deleteItem(item: ShoppingItem) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }

    fun deleteCompletedItems() {
        viewModelScope.launch {
            repository.deleteCompleted()
        }
    }
}