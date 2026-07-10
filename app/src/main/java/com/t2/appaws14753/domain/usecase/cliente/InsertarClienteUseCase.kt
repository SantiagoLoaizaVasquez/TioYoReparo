package com.t2.appaws14753.domain.usecase.cliente

import com.t2.appaws14753.domain.model.Cliente
import com.t2.appaws14753.domain.repository.ClienteRepository

class InsertarClienteUseCase(private val repo: ClienteRepository) {
    suspend operator fun invoke(cliente: Cliente) =repo.insert(cliente)
}