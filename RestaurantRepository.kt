package com.example.skku_restaurant.repository

import com.example.skku_restaurant.api.RetrofitClient
import com.example.skku_restaurant.model.Restaurant

class RestaurantRepository {
    private val api = RetrofitClient.retrofit.create(com.example.skku_restaurant.api.RestaurantApiService::class.java)

    suspend fun getAllRestaurants(): List<Restaurant> {
        return api.getAllRestaurants().restaurants
    }

    suspend fun getRestaurantsByCategory(category: String): List<Restaurant> {
        return api.getRestaurantsByCategory(category).restaurants
    }
}

