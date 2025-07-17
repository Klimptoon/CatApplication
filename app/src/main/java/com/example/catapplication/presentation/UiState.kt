package com.example.catapplication.presentation

import com.example.catapplication.domain.CatModel

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<CatModel>) : UiState()
    data class Error(val message: String) : UiState()
}