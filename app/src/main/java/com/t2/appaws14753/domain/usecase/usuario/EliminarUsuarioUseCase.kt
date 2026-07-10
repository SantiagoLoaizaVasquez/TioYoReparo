package com.t2.appaws14753.domain.usecase.usuario

import com.t2.appaws14753.domain.model.Usuario
import com.t2.appaws14753.domain.repository.UsuarioRepository


class EliminarUsuarioUseCase(private val repo: UsuarioRepository) {
    suspend operator fun invoke(usuario: Usuario) {
        if(usuario.usuarioId.isBlank()) throw IllegalArgumentException("Usuario inválido para eliminar")
        repo.delete(usuario)
    }
}
