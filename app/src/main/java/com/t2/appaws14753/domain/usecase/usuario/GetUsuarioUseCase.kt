package com.t2.appaws14753.domain.usecase.usuario

import com.t2.appaws14753.domain.model.Usuario
import com.t2.appaws14753.domain.repository.UsuarioRepository


class GetUsuarioUseCase(private val repo: UsuarioRepository) {
    suspend operator fun invoke(): List<Usuario> {
        return repo.getall()
    }
}
