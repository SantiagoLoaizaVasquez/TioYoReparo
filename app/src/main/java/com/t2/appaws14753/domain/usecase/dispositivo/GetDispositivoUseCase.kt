package com.t2.appaws14753.domain.usecase.dispositivo

import com.t2.appaws14753.domain.repository.DispositivoRepository

class GetDispositivoUseCase(private val repo: DispositivoRepository) {
    suspend operator fun invoke() = repo.getAll()
}
