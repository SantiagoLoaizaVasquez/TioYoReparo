package com.t2.appaws14753.presentation.state

import com.t2.appaws14753.domain.model.Cliente


data class ClienteUiState(
    val listClientes: List<Cliente> = emptyList(),
    val isLoading: Boolean = false,
    val search: String = ""

)
