package com.example.catapplication.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapplication.domain.CatModel
import com.example.catapplication.domain.usecases.AddToFavoritesUseCase
import com.example.catapplication.domain.usecases.DeleteFromFavoritesUseCase
import com.example.catapplication.domain.usecases.GetCatsUseCase
import com.example.catapplication.domain.usecases.GetFavoritesCatsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val getCatsUseCase: GetCatsUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val deleteFromFavoritesUseCase: DeleteFromFavoritesUseCase,
    private val getFavoritesCatsUseCase: GetFavoritesCatsUseCase,
) : ViewModel() {
    private val _cats = MutableStateFlow<List<CatModel>>(emptyList())
    val cats: StateFlow<List<CatModel>> = _cats

    private val _favorite = MutableStateFlow<List<CatModel>>(emptyList())
    val favorite : StateFlow<List<CatModel>> = _favorite

    init {
        viewModelScope.launch {
            getFavoritesCatsUseCase.invoke().collect {
                cats -> _favorite.value = cats
            }
        }

        viewModelScope.launch {
            if (_cats.value.isEmpty()) {
                loadCats()
            }
        }
    }

    fun addCat(catModel: CatModel) {
        viewModelScope.launch {
            addToFavoritesUseCase.invoke(catModel)
        }
    }

    fun deleteFromFavorites(catModel: CatModel) {
        viewModelScope.launch {
            deleteFromFavoritesUseCase.invoke(cat = catModel)
        }
    }


    fun loadCats() {
        Log.e("MainViewModel", "зашел в котов")
        viewModelScope.launch {
            _cats.value = getCatsUseCase.invoke()
        }
    }
}