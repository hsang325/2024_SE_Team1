package com.example.skku_restaurant.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skku_restaurant.model.Restaurant
import com.example.skku_restaurant.repository.RestaurantRepository
import kotlinx.coroutines.launch


class RestaurantViewModel : ViewModel() {
    private val repository = RestaurantRepository()
    private val _restaurants = MutableLiveData<List<Restaurant>>()
    val restaurants: LiveData<List<Restaurant>> get() = _restaurants

    fun fetchRestaurants() {
        viewModelScope.launch {
            _restaurants.value = repository.getAllRestaurants()
        }
    }

    fun fetchRestaurantsByCategory(category: String) {
        viewModelScope.launch {
            _restaurants.value = repository.getRestaurantsByCategory(category)
        }
    }
}
