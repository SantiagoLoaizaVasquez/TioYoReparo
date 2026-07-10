package com.t2.appaws14753.domain.usecase.orden

import com.t2.appaws14753.domain.model.Orden
import com.t2.appaws14753.domain.repository.OrdenRepository


class GetOrdenUseCase(private val repo: OrdenRepository) {
    suspend operator fun invoke(): List<Orden> {
        return repo.getAll()
    }
}
