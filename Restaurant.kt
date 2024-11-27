package com.example.skku_restaurant.model

data class Restaurant(
    val restaurant_id: Int,
    val name: String,
    val address: String,
    val category: String,
    val created_by: Int,
    val tags: String,
    val created_at: String,
    val updated_at: String
)
