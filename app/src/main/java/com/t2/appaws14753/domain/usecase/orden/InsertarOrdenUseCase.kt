package com.t2.appaws14753.domain.usecase.orden

import com.t2.appaws14753.domain.model.Orden
import com.t2.appaws14753.domain.repository.OrdenRepository


class InsertarOrdenUseCase(private val repo: OrdenRepository) {
    suspend operator fun invoke(orden: Orden) {
        if(orden.dispositivoId.isBlank()) throw IllegalArgumentException("Dispositivo inválido")
        if(orden.clienteId.isBlank()) throw IllegalArgumentException("Cliente inválido")
        if(orden.tecnicoId.isBlank()) throw IllegalArgumentException("Técnico inválido")
        if(orden.estado.isBlank()) throw IllegalArgumentException("Estado inválido")
        if(orden.prioridad.isBlank()) throw IllegalArgumentException("Prioridad inválida")
        if(orden.totalCobrado < 0) throw IllegalArgumentException("Total cobrado inválido")
        repo.insert(orden)
    }
}
