package com.t2.appaws14753.domain.usecase.orden

import com.t2.appaws14753.domain.model.Orden
import com.t2.appaws14753.domain.repository.OrdenRepository


class EliminarOrdenUseCase(private val repo: OrdenRepository) {
    suspend operator fun invoke(orden: Orden) {
        if(orden.ordenId.isBlank()) throw IllegalArgumentException("Orden inválida para eliminar")
        repo.delete(orden)
    }
}
