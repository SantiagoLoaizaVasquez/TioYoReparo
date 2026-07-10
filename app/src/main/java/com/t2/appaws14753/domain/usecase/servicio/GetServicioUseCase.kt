package com.t2.appaws14753.domain.usecase.servicio

import com.t2.appaws14753.domain.model.Servicio
import com.t2.appaws14753.domain.repository.ServicioRepository


class GetServicioUseCase(private val repo: ServicioRepository) {
    suspend operator fun invoke(): List<Servicio>{
        return repo.getAll()
    }
}