package com.example.catapplication.di

import android.content.Context
import androidx.room.Room
import com.example.catapplication.data.db.CatDao
import com.example.catapplication.data.db.CatDatabase
import com.example.catapplication.data.network.CatApi
import com.example.catapplication.data.repo.DataRepositoryImpl
import com.example.catapplication.domain.DataRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): CatDatabase {
        return Room.databaseBuilder(context, CatDatabase::class.java, "cats_db").build()
    }

    @Provides
    fun provideDao(database: CatDatabase): CatDao {
        return database.catDao()
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://api.thecatapi.com/").addConverterFactory(
            GsonConverterFactory.create()
        ).build()
    }

    @Provides
    @Singleton
    fun provideCatApi(retrofit: Retrofit): CatApi {
        return retrofit.create(CatApi::class.java)
    }

    @Provides
    fun provideDataRepository(implementation: DataRepositoryImpl): DataRepository {
        return implementation
    }
}