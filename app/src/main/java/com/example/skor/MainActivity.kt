package com.example.skor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.skor.ShoppingDatabase
import com.example.skor.ShoppingRepository
import com.example.skor.ShoppingListViewModel
import com.example.skor.ShoppingViewModelFactory

class MainActivity : AppCompatActivity() {

    private val viewModel: ShoppingListViewModel by viewModels {
        val database = ShoppingDatabase.getDatabase(this)
        val repository = ShoppingRepository(database.shoppingDao())
        ShoppingViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        val addButton: Button = findViewById(R.id.button_add_product)
        val viewListButton: Button = findViewById(R.id.button_view_list)
        val nameEditText: EditText = findViewById(R.id.edit_text_product_name)
        val quantityEditText: EditText = findViewById(R.id.edit_text_product_quantity)

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

        // КНОПКА ПЕРЕХОДА НА ЭКРАН СПИСКА
        viewListButton.setOnClickListener {
            val intent = Intent(this, ShoppingListActivity::class.java)
            startActivity(intent)
        }
    }
}