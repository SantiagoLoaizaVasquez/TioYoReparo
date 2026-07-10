package com.t2.appaws14753.domain.usecase.dispositivo

import com.t2.appaws14753.domain.repository.DispositivoRepository

class GetDispositivoByIdUseCase(private val repo: DispositivoRepository) {
    suspend operator fun invoke(id: String) = repo.getById(id)
}
