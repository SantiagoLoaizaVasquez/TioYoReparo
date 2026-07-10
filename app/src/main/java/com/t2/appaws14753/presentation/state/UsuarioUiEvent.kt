package com.t2.appaws14753.presentation.state


sealed class UsuarioUiEvent{
    data class MostrarSnackBar(val mensaje: String): UsuarioUiEvent()
    object NavigationBack: UsuarioUiEvent()
}
