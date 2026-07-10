package com.t2.appaws14753.domain.usecase.servicio



data class ServicioUseCases(
    val getServicios: GetServicioUseCase,
    val getServicioById: GetServicioByIdUseCase,
    val insertarServicio: InsertarServicioUseCase,
    val actualizarServicio: UpdateServicioUseCase,
    val eliminarServicio: EliminarServicioUseCase
)
