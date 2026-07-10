package com.t2.appaws14753.presentation.state


sealed class DispositivoUiEvent{
    data class MostrarSnackBar(val mensaje: String): DispositivoUiEvent()
    object NavigationBack: DispositivoUiEvent()
}