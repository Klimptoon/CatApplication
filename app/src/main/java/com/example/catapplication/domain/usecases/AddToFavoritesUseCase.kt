package com.example.catapplication.domain.usecases

import com.example.catapplication.domain.CatModel
import com.example.catapplication.domain.DataRepository
import javax.inject.Inject

class AddToFavoritesUseCase @Inject constructor(private val repository: DataRepository) {
    suspend fun invoke(cat: CatModel) {
        repository.addToFavorites(cat = cat)
    }
}