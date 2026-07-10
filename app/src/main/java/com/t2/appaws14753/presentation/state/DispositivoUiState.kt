package com.t2.appaws14753.presentation.state

import com.t2.appaws14753.domain.model.Dispositivo


data class DispositivoUiState(
    val listDispositivos: List<Dispositivo> = emptyList(),
    val isLoading: Boolean = false,
    val search: String = ""

)