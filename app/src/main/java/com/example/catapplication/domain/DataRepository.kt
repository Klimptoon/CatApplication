package com.example.catapplication.domain

import kotlinx.coroutines.flow.Flow

interface DataRepository  {
    suspend fun getCats(): List<CatModel>
    suspend fun addToFavorites(cat: CatModel)
    suspend fun deleteFromFavorites(cat: CatModel)
    fun getFavoritesCats() : Flow<List<CatModel>>
}