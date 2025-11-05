package com.example.skor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skor.R
import com.example.skor.ShoppingItem

class ShoppingListAdapter(
    private val onItemCheck: (ShoppingItem, Boolean) -> Unit,
    private val onItemDelete: (ShoppingItem) -> Unit
) : ListAdapter<ShoppingItem, ShoppingListAdapter.ShoppingItemViewHolder>(DiffCallback) {

    class ShoppingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.text_view_item_name)
        private val quantityTextView: TextView = itemView.findViewById(R.id.text_view_item_quantity)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkbox_item)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.button_delete)

        fun bind(item: ShoppingItem, onItemCheck: (ShoppingItem, Boolean) -> Unit, onItemDelete: (ShoppingItem) -> Unit) {
            nameTextView.text = item.name
            quantityTextView.text = "Количество: ${item.quantity}"
            checkBox.isChecked = item.isPurchased

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                onItemCheck(item, isChecked)
            }

            deleteButton.setOnClickListener {
                onItemDelete(item)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shopping, parent, false)
        return ShoppingItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, onItemCheck, onItemDelete)
    }
}