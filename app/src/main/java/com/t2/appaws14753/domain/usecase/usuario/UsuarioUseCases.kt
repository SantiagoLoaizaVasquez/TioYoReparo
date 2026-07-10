package com.t2.appaws14753.domain.usecase.usuario


data class UsuarioUseCases(
    val getUsuarios: GetUsuarioUseCase,
    val getUsuarioById: GetUsuarioByIdUseCase,
    val insertarUsuario: InsertarUsuarioUseCase,
    val actualizarUsuario: ActualizarUsuarioUseCase,
    val eliminarUsuario: EliminarUsuarioUseCase
)
