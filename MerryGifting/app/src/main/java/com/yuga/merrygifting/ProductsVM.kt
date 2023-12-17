package com.yuga.merrygifting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsVM @Inject constructor(private val repository: ProductsRepository) : ViewModel() {

    val products = MutableStateFlow<List<Product>>(listOf())


    init {
        viewModelScope.launch {
            repository.deleteAll()
            repository.insertProducts()
            getAllProducts()
        }
    }

    private fun getAllProducts() {
        viewModelScope.launch {
            products.value = repository.getAllProducts()
        }
    }

}

class ProductsRepository @Inject constructor(private val dao: ProductDao) {


    suspend fun insertProducts() {
        dao.insertProduct(Product(0, "Winter Wear", "Sweater", "description", 10.0, "sweater.jpeg"))
        dao.insertProduct(Product(0, "Winter Wear", "Winter Caps", "description", 5.0, "image"))
        dao.insertProduct(Product(0, "Foot Wear", "Snow Boots", "description", 15.0, "image"))
        dao.insertProduct(Product(0, "Bags", "Bag", "description", 55.0, "image"))
        dao.insertProduct(Product(0, "Perfumes", "Perfume", "description", 30.0, "image"))
        dao.insertProduct(Product(0, "Foot Wear", "Sneakers", "description", 150.0, "image"))
        dao.insertProduct(Product(0, "Foot Wear", "Sandals", "description", 35.0, "image"))
        dao.insertProduct(Product(0, "Foot Wear", "Socks", "description", 5.0, "image"))
    }

    suspend fun getAllProducts(): List<Product> {
        return dao.getAllProducts()
    }

    suspend fun deleteAll() {
        dao.deleteAllProducts()
    }

}