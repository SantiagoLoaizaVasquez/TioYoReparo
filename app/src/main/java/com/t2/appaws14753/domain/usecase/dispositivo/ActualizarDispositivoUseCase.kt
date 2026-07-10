package com.t2.appaws14753.domain.usecase.dispositivo

import com.t2.appaws14753.domain.model.Dispositivo
import com.t2.appaws14753.domain.repository.DispositivoRepository

class ActualizarDispositivoUseCase(private val repo: DispositivoRepository) {
    suspend operator fun invoke(dispositivo: Dispositivo) = repo.update(dispositivo)
}
