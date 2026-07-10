package com.t2.appaws14753.presentation.state

import com.t2.appaws14753.domain.model.Orden


data class OrdenUiState(
    val listOrdenes: List<Orden> = emptyList(),
    val isLoading: Boolean = false,
    val search: String = ""

)
