package com.example.catapplication.domain

import com.example.catapplication.data.db.CatEntity
import com.example.catapplication.data.models.CatDto

data class CatModel(
    val id: String,
    val url: String,
)


fun CatModel.toDto(): CatDto {
    return CatDto(
        id = this.id,
        url = this.url
    )
}

fun CatModel.toEntity(): CatEntity {
    return CatEntity(
        id = this.id,
        url = this.url
    )
}