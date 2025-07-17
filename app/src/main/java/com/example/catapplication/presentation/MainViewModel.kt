package com.example.catapplication.presentation

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
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _favorite = MutableStateFlow<List<CatModel>>(emptyList())
    val favorite: StateFlow<List<CatModel>> = _favorite

    init {
        viewModelScope.launch {
            getFavoritesCatsUseCase.invoke().collect { cats ->
                _favorite.value = cats
            }
        }

        viewModelScope.launch {
            val currentState = uiState.value
            if (currentState !is UiState.Success || currentState.data.isEmpty()) {
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
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val cats = getCatsUseCase.invoke() // может быть suspend
                _uiState.value = UiState.Success(cats)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
