package com.t2.appaws14753.presentation.state

import com.t2.appaws14753.domain.model.Usuario


data class UsuarioUiState(
    val listUsuarios: List<Usuario> = emptyList(),
    val isLoading: Boolean = false,
    val search: String = ""

)
