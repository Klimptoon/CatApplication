package com.example.catapplication.domain.usecases

import com.example.catapplication.domain.CatModel
import com.example.catapplication.domain.DataRepository
import javax.inject.Inject

class GetCatsUseCase @Inject constructor(private val repository: DataRepository) {
    suspend fun invoke() : List<CatModel> {
        return repository.getCats()
    }
}