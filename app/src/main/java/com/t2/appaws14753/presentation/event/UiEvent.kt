package com.t2.appaws14753.presentation.event

sealed interface UiEvent {
    data class SUCCESS(val mensaje: String): UiEvent
    data class ERROR(val mensaje: String): UiEvent
    data class WARNING(val mensaje: String): UiEvent
}
