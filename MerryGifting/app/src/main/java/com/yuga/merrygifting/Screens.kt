package com.yuga.merrygifting

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun LoginScreen(navController: NavController, viewModel: LoginVM = hiltViewModel()) {
    val context = LocalContext.current
    var number by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_shopping_basket_24),
            contentDescription = "Logo",
            modifier = Modifier
                .size(250.dp)
                .align(CenterHorizontally)
                .padding(20.dp)
        )

        TextField(
            value = number,
            onValueChange = { number = it },
            label = { Text("USER ID") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(10.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("PASSWORD") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(10.dp))

        Button(onClick = {
            if (viewModel.login(number, password)) {
                Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                navController.navigate("products")
            } else {
                Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Click Here")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    navController: NavController,
    viewModel: ProductsVM = hiltViewModel(),
    cartVM: CartVM = hiltViewModel()
) {
    val products = viewModel.products.collectAsState()
    val categories = getUniqueCategories(products.value)

    var selectedCategory by remember { mutableStateOf(categories.firstOrNull()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(text = "Merry Gifting", color = Color.White)
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),

            actions = {
                IconButton(
                    onClick = {
                        navController.navigate("cart")
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_shopping_cart_24),
                        contentDescription = "Cart"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            categories.forEach { category ->

                Chip(
                    text = category,
                    selected = category == selectedCategory,
                    onClick = {
                        selectedCategory = category
                    },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        val filteredProducts = filterProductsByCategory(products.value, selectedCategory)
        filteredProducts.forEach { product ->
            ProductView(product = product, cartVM = cartVM)
            Spacer(modifier = Modifier.padding(10.dp))
        }
    }
}

fun getUniqueCategories(products: List<Product>): List<String> {
    Log.e("Products", "getUniqueCategories: $products")
    Log.e("Products", "getUniqueCategories: ${products.map { it.category }}")
    return products.map { it.category }.distinct()
}

@Composable
fun Chip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                if (selected) Color.Gray else Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}


fun filterProductsByCategory(products: List<Product>, category: String?): List<Product> {
    return if (category != null) {
        products.filter { it.category == category }
    } else {
        products
    }
}


@Composable
fun ProductView(product: Product, cartVM: CartVM) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_shopping_basket_24),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(10.dp)
                    .align(Alignment.CenterVertically)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            ) {
                Text(text = product.name, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "$${product.price}")
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        cartVM.addToCart(product)
                        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add to cart")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartVM: CartVM = hiltViewModel()
) {
    val cartItems = cartVM.cartItems.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(text = "Cart", color = Color.White)
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.primary,
            )
        )
        Spacer(modifier = Modifier.height(30.dp))

        Column(modifier = Modifier.padding(16.dp)) {
            if (cartItems.value.isEmpty()) {
                Text(
                    text = "Cart empty.",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
            } else {
                cartItems.value.forEach { cartItem ->
                    CartItemView(cartItem = cartItem)
                    Divider()
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Total: $${cartVM.getTotalPrice()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        cartVM.clearCart()
                        navController.navigate("order")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Place Order")
                }
            }
        }
    }
}

@Composable
fun CartItemView(cartItem: CartItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "${cartItem.product.name} x${cartItem.quantity}")
            Text(text = "$${cartItem.product.price * cartItem.quantity}")
        }
    }
}


@Composable
fun OrderScreen(navController: NavController) {
    val orderNumber = generateRandomOrderNumber()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_sentiment_very_satisfied_24),
            contentDescription = "Order Success Image",
            modifier = Modifier
                .size(120.dp)
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Order Placed Successfully!",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your order number is #$orderNumber",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("products")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue Shopping")
        }
    }
}

fun generateRandomOrderNumber(): String {
    return (1000..9999).random().toString()
}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("products") {
            ProductScreen(navController = navController)
        }
        composable("cart") {
            CartScreen(navController = navController)
        }
        composable("order") {
            OrderScreen(navController = navController)
        }
    }
}