package com.t2.appaws14753.presentation.state


sealed class OrdenUiEvent{
    data class MostrarSnackBar(val mensaje: String): OrdenUiEvent()
    object NavigationBack: OrdenUiEvent()
}
