package com.example.catapplication.data.network

import com.example.catapplication.data.models.CatDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CatApi {
    @GET("v1/images/search")
    suspend fun getCats(@Query("limit") limit: Int = 10): List<CatDto>
}