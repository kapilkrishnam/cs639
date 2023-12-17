package com.yuga.merrygifting

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val name: String,
    val description: String,
    val price: Double,
    val image: String
)

@Dao
interface ProductDao {

    @androidx.room.Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<Product>

    @Insert
    suspend fun insertProduct(product: Product)

    @androidx.room.Query("DELETE FROM products")
    suspend fun deleteAllProducts()
}


@Database(entities = [Product::class], version = 1)
abstract class ProductDataBase : androidx.room.RoomDatabase() {
    abstract fun productsDao(): ProductDao
}