package com.t2.appaws14753.domain.usecase.servicio

import com.t2.appaws14753.domain.model.Servicio
import com.t2.appaws14753.domain.repository.ServicioRepository



class EliminarServicioUseCase(private val repo: ServicioRepository) {
    suspend operator fun invoke(servicio: Servicio){
        if(servicio.servicioId.isBlank()) throw IllegalArgumentException("Servicio inválido para eliminar")
        repo.delete(servicio)
    }
}