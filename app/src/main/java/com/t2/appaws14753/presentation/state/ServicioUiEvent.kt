package com.t2.appaws14753.presentation.state


sealed class ServicioUiEvent{
    data class MostrarSnackBar(val mensaje: String): ServicioUiEvent()
    object NavigationBack: ServicioUiEvent()
}
