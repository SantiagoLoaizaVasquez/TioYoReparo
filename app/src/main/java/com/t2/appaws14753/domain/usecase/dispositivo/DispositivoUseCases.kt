package com.t2.appaws14753.domain.usecase.dispositivo

data class DispositivoUseCases(
    val getDispositivos: GetDispositivoUseCase,
    val getDispositivoById: GetDispositivoByIdUseCase,
    val insertarDispositivo: InsertarDispositivoUseCase,
    val actualizarDispositivo: ActualizarDispositivoUseCase,
    val eliminarDispositivo: EliminarDispositivoUseCase
)
