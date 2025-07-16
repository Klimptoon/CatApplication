package com.example.catapplication.data.repo

import com.example.catapplication.data.db.CatDao
import com.example.catapplication.data.db.toDomain
import com.example.catapplication.data.models.toDomain
import com.example.catapplication.data.network.CatApi
import com.example.catapplication.domain.CatModel
import com.example.catapplication.domain.DataRepository
import com.example.catapplication.domain.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(private val api: CatApi, private val dao: CatDao) : DataRepository {
    override suspend fun getCats(): List<CatModel> {
        return api.getCats().map { it.toDomain() }
    }

    override suspend fun addToFavorites(cat: CatModel) {
        dao.insert(cat = cat.toEntity())
    }

    override suspend fun deleteFromFavorites(cat: CatModel) {
        dao.delete(cat = cat.toEntity())
    }

    override fun getFavoritesCats(): Flow<List<CatModel>> {
        return dao.getFavoritesCats().map { list ->
            list.map { it.toDomain() }
        }
    }
}