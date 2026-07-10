package com.t2.appaws14753.domain.usecase.cliente

data class ClienteUseCases(
    val getClientes: GetClienteUseCase,
    val getClienteById: GetClienteByIdUseCase,
    val insertarCliente: InsertarClienteUseCase,
    val actualizarCliente: ActualizarClienteUseCase,
    val eliminarCliente: EliminarClienteUseCase
)