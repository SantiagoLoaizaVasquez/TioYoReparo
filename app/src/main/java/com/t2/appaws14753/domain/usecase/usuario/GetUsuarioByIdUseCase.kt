package com.t2.appaws14753.domain.usecase.usuario

import com.t2.appaws14753.domain.model.Usuario
import com.t2.appaws14753.domain.repository.UsuarioRepository


class GetUsuarioByIdUseCase(private val repo: UsuarioRepository) {
    suspend operator fun invoke(id: String): Usuario {
        if(id.isBlank()) throw IllegalArgumentException("Id inválido")
        return repo.getbyid(id) ?: throw NoSuchElementException("Usuario no encontrado")
    }
}
