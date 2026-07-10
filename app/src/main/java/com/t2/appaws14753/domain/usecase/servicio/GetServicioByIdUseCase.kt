package com.t2.appaws14753.domain.usecase.servicio

import com.t2.appaws14753.domain.model.Servicio
import com.t2.appaws14753.domain.repository.ServicioRepository


class GetServicioByIdUseCase (private val repo: ServicioRepository){
    suspend operator fun invoke (id: String): Servicio{
        if(id.isBlank()) throw IllegalArgumentException("Id inválido")
        return repo.getById(id) ?: throw NoSuchElementException("Servicio no encontrado")
    }
}