package com.example.catapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.catapplication.domain.usecases.AddToFavoritesUseCase
import com.example.catapplication.domain.usecases.DeleteFromFavoritesUseCase
import com.example.catapplication.domain.usecases.GetCatsUseCase
import com.example.catapplication.domain.usecases.GetFavoritesCatsUseCase
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(
    private val getCatsUseCase: GetCatsUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val deleteFromFavoritesUseCase: DeleteFromFavoritesUseCase,
    private val getFavoritesCatsUseCase: GetFavoritesCatsUseCase,
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                getCatsUseCase,
                addToFavoritesUseCase,
                deleteFromFavoritesUseCase,
                getFavoritesCatsUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}