package com.example.skor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.lifecycleScope
import com.example.skor.ShoppingDatabase
import com.example.skor.ShoppingRepository
import com.example.skor.ShoppingListViewModel
import com.example.skor.ShoppingViewModelFactory
import kotlinx.coroutines.launch

class AddItemFragment : Fragment() {

    private val viewModel: ShoppingListViewModel by activityViewModels {
        val database = ShoppingDatabase.getDatabase(requireContext())
        val repository = ShoppingRepository(database.shoppingDao())
        ShoppingViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addButton: Button = view.findViewById(R.id.button_add_product)
        val viewListButton: Button = view.findViewById(R.id.button_view_list)
        val nameEditText: EditText = view.findViewById(R.id.edit_text_product_name)
        val quantityEditText: EditText = view.findViewById(R.id.edit_text_product_quantity)

        addButton.setOnClickListener {
            val productName = nameEditText.text.toString()
            val productQuantity = quantityEditText.text.toString().toIntOrNull() ?: 1

            if (productName.isNotEmpty()) {
                viewModel.addNewItem(productName, productQuantity)
                nameEditText.text.clear()
                quantityEditText.text.clear()

                Toast.makeText(
                    requireContext(),
                    "Товар '$productName' добавлен!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Введите название товара!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewListButton.setOnClickListener {
            // Навигация к списку товаров
            findNavController().navigate(R.id.action_addItemFragment_to_listFragment)
        }
    }
}