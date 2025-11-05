package com.example.skor

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val quantity: Int,
    @ColumnInfo(name = "is_purchased", defaultValue = "0")
    val isPurchased: Boolean = false
)