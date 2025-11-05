package com.example.skor

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skor.ShoppingDatabase
import com.example.skor.ShoppingRepository
import com.example.skor.ShoppingListAdapter
import com.example.skor.ShoppingListViewModel
import com.example.skor.ShoppingViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_main)

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
        val addButton: Button = findViewById(R.id.button_add_product)
        val nameEditText: EditText = findViewById(R.id.edit_text_product_name)
        val quantityEditText: EditText = findViewById(R.id.edit_text_product_quantity)
        val deleteCompletedButton: Button = findViewById(R.id.button_delete_completed)

        addButton.setOnClickListener {
            val productName = nameEditText.text.toString()
            val productQuantity = quantityEditText.text.toString().toIntOrNull() ?: 1

            if (productName.isNotEmpty()) {
                viewModel.addNewItem(productName, productQuantity)
                nameEditText.text.clear()
                quantityEditText.text.clear()

                Toast.makeText(
                    this,
                    "Товар '$productName' добавлен!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Введите название товара!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        deleteCompletedButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.deleteCompletedItems()
                Toast.makeText(
                    this@MainActivity,
                    "Завершенные товары удалены",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeData() {
        viewModel.shoppingItems.observe(this) { items ->
            adapter.submitList(items)
        }
    }
}