package com.yuga.merrygifting

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ProductDataBase {
        return Room.databaseBuilder(
            context,
            ProductDataBase::class.java,
            "merry_gifting"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideProductDao(productDataBase: ProductDataBase): ProductDao {
        return productDataBase.productsDao()
    }

    @Provides
    @Singleton
    fun provideProductRepo(dao: ProductDao): ProductsRepository {
        return ProductsRepository(dao = dao)
    }

    @Provides
    @Singleton
    fun provideLoginRepo(): LoginRepository {
        return LoginRepository()
    }

    @Provides
    @Singleton
    fun provideCartVM(productsRepository: ProductsRepository): CartVM {
        return CartVM(productsRepository)
    }

}