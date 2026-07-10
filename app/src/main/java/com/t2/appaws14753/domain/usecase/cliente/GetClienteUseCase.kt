package com.t2.appaws14753.domain.usecase.cliente

import com.t2.appaws14753.domain.repository.ClienteRepository

class GetClienteUseCase(private val repo: ClienteRepository) {
    operator fun invoke() = repo.getAll()
}