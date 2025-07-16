package com.example.catapplication.domain.usecases

import com.example.catapplication.domain.CatModel
import com.example.catapplication.domain.DataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesCatsUseCase @Inject constructor(private val repository: DataRepository) {
    fun invoke() : Flow<List<CatModel>> {
        return repository.getFavoritesCats()
    }
}