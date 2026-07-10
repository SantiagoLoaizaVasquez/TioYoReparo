package com.t2.appaws14753.domain.usecase.orden


data class OrdenUseCases(
    val getOrdenes: GetOrdenUseCase,
    val getOrdenById: GetOrdenByIdUseCase,
    val insertarOrden: InsertarOrdenUseCase,
    val actualizarOrden: ActualizarOrdenUseCase,
    val eliminarOrden: EliminarOrdenUseCase
)
