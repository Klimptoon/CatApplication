package com.example.catapplication.data.models

import com.example.catapplication.domain.CatModel

data class CatDto(
    val id: String,
    val url: String
)

fun CatDto.toDomain(): CatModel {
    return CatModel(
        id = this.id,
        url = this.url
    )
}