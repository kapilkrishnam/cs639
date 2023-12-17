package com.yuga.merrygifting

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CartVM @Inject constructor(private val  repository: ProductsRepository) : ViewModel() {

    val cartItems = MutableStateFlow<List<CartItem>>(listOf())

    fun addToCart(product: Product) {
        val existingItem = cartItems.value.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity += 1
        } else {
            cartItems.value = cartItems.value + CartItem(product, 1)
        }
    }


    fun getTotalPrice(): Double {
        return cartItems.value.sumOf { it.product.price * it.quantity }
    }

    fun clearCart() {
        cartItems.value = listOf()
    }

}

data class CartItem(val product: Product, var quantity: Int)
