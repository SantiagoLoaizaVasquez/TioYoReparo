package com.t2.appaws14753.domain.usecase.orden

import com.t2.appaws14753.domain.model.Orden
import com.t2.appaws14753.domain.repository.OrdenRepository


class GetOrdenByIdUseCase(private val repo: OrdenRepository) {
    suspend operator fun invoke(id: String): Orden {
        if(id.isBlank()) throw IllegalArgumentException("Id inválido")
        return repo.getById(id) ?: throw NoSuchElementException("Orden no encontrada")
    }
}
