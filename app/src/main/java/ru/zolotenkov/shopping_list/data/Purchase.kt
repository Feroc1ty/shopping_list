package ru.zolotenkov.shopping_list.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchase")
data class Purchase (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)
