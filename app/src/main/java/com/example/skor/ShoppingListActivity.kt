package com.example.skor

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skor.ShoppingDatabase
import com.example.skor.ShoppingRepository
import com.example.skor.ShoppingListAdapter
import com.example.skor.ShoppingListViewModel
import com.example.skor.ShoppingViewModelFactory
import kotlinx.coroutines.launch

class ShoppingListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShoppingListAdapter

    // Инициализация ViewModel с фабрикой
    private val viewModel: ShoppingListViewModel by viewModels {
        val database = ShoppingDatabase.getDatabase(this)
        val repository = ShoppingRepository(database.shoppingDao())
        ShoppingViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        setupRecyclerView()
        setupClickListeners()
        observeData()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        adapter = ShoppingListAdapter(
            onItemCheck = { item, isChecked ->
                val updatedItem = item.copy(isPurchased = isChecked)
                viewModel.updateItem(updatedItem)
            },
            onItemDelete = { item ->
                viewModel.deleteItem(item)
            }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupClickListeners() {
        val deleteCompletedButton: Button = findViewById(R.id.button_delete_completed)
        val backButton: Button = findViewById(R.id.button_back)

        deleteCompletedButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.deleteCompletedItems()
                Toast.makeText(
                    this@ShoppingListActivity,
                    "Завершенные товары удалены",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // КНОПКА ВОЗВРАТА НА ГЛАВНЫЙ ЭКРАН
        backButton.setOnClickListener {
            finish() // Закрываем текущую активность и возвращаемся назад
        }
    }

    private fun observeData() {
        viewModel.shoppingItems.observe(this) { items ->
            adapter.submitList(items)
        }
    }
}