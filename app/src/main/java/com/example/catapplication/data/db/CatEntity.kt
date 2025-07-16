package com.example.catapplication.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.catapplication.domain.CatModel

@Entity(tableName = "favorites")
data class CatEntity(
    @PrimaryKey
    val id: String,
    val url: String,
)

fun CatEntity.toDomain(): CatModel {
    return CatModel(id = this.id, url = this.url)
}