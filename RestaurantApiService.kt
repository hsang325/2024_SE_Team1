package com.example.skku_restaurant.api

import com.example.skku_restaurant.model.RestaurantResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantApiService {
    @GET("/api/restaurants")
    suspend fun getAllRestaurants(): RestaurantResponse

    @GET("/api/restaurants")
    suspend fun getRestaurantsByCategory(@Query("category") category: String): RestaurantResponse
}
