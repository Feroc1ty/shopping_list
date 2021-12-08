package ru.zolotenkov.shopping_list

data class Product(
    val userId: Int? = null,
    val productId: Int? = null,
    val product: String? = null,
    val status: Int? = null
)
