package com.t2.appaws14753.domain.usecase.cliente

import com.t2.appaws14753.domain.repository.ClienteRepository

class EliminarClienteUseCase(private val repo: ClienteRepository) {
    suspend operator fun invoke(id: Int)=repo.delete(id)
}