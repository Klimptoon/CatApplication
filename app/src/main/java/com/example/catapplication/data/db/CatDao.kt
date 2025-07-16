package com.example.catapplication.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cat: CatEntity)

    @Delete
    suspend fun delete(cat: CatEntity)

    @Query("SELECT * FROM favorites")
    fun getFavoritesCats() : Flow<List<CatEntity>>

}