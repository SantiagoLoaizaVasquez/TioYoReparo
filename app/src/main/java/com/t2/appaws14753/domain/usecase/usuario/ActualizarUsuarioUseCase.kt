package com.t2.appaws14753.domain.usecase.usuario

import com.t2.appaws14753.domain.model.Usuario
import com.t2.appaws14753.domain.repository.UsuarioRepository


class ActualizarUsuarioUseCase(private val repo: UsuarioRepository) {
    suspend operator fun invoke(usuario: Usuario) {
        if(usuario.usuarioId.isBlank()) throw IllegalArgumentException("ID inválido")
        if(usuario.rol.isBlank()) throw IllegalArgumentException("Rol inválido")
        if(usuario.correo.isBlank()) throw IllegalArgumentException("Correo vacío")
        if(usuario.nombres.isBlank()) throw IllegalArgumentException("Nombres vacíos")
        if(usuario.apellidoPaterno.isBlank()) throw IllegalArgumentException("Apellido paterno vacío")
        repo.update(usuario)
    }
}
