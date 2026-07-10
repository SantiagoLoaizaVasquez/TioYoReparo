package com.t2.appaws14753.presentation.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object EventBus {
    private val _eventos = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventos = _eventos.asSharedFlow()

    suspend fun enviar(event: UiEvent){
        _eventos.emit(event)
    }
}
