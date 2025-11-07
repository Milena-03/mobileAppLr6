package com.example.skor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skor.ShoppingDatabase
import com.example.skor.ShoppingRepository
import com.example.skor.ShoppingListAdapter
import com.example.skor.ShoppingListViewModel
import com.example.skor.ShoppingViewModelFactory
import kotlinx.coroutines.launch

class ListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShoppingListAdapter

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
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)
        setupClickListeners(view)
        observeData()
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
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
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupClickListeners(view: View) {
        val deleteCompletedButton: Button = view.findViewById(R.id.button_delete_completed)

        deleteCompletedButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.deleteCompletedItems()
                Toast.makeText(
                    requireContext(),
                    "Завершенные товары удалены",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeData() {
        viewModel.shoppingItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }
    }
}