package com.t2.appaws14753.presentation.state

import com.t2.appaws14753.domain.model.Servicio


data class ServicioUiState(
    val listServicios: List<Servicio> = emptyList(),
    val isLoading: Boolean = false,
    val search: String = ""

)
