package com.t2.appaws14753.presentation.state


sealed class ClienteUiEvent{
    data class MostrarSnackBar(val mensaje: String): ClienteUiEvent()
    object NavigationBack: ClienteUiEvent()
}
