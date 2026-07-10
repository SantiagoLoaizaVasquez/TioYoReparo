package com.t2.appaws14753.domain.usecase.cliente

import com.t2.appaws14753.domain.model.Cliente
import com.t2.appaws14753.domain.repository.ClienteRepository

class ActualizarClienteUseCase(private val repo: ClienteRepository){
    suspend operator fun invoke(cliente: Cliente)=repo.update(cliente)
}