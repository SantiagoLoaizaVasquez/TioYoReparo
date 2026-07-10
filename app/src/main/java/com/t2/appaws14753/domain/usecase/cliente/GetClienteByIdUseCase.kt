package com.t2.appaws14753.domain.usecase.cliente

import com.t2.appaws14753.domain.repository.ClienteRepository

class GetClienteByIdUseCase(private val repo: ClienteRepository) {
    suspend operator fun invoke(id: Int) = repo.getById(id)
}