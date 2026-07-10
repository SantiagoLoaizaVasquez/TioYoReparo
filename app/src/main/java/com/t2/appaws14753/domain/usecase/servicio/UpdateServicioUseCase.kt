package com.t2.appaws14753.domain.usecase.servicio

import com.t2.appaws14753.domain.model.Servicio
import com.t2.appaws14753.domain.repository.ServicioRepository


class UpdateServicioUseCase(private val repo: ServicioRepository) {
    suspend operator fun invoke(servicio: Servicio){
        if(servicio.servicioId.isBlank()) throw IllegalArgumentException("ID inválido")
        if(servicio.nombreServicio.isBlank()) throw IllegalArgumentException("Nombre de servicio vacío")
        if(servicio.precioServicio <= 0) throw IllegalArgumentException("Precio inválido")
        repo.update(servicio)
    }
}