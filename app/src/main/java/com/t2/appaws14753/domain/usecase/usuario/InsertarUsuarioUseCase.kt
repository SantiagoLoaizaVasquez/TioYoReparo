package com.t2.appaws14753.domain.usecase.usuario

import com.t2.appaws14753.domain.model.Usuario
import com.t2.appaws14753.domain.repository.UsuarioRepository


class InsertarUsuarioUseCase(private val repo: UsuarioRepository) {
    suspend operator fun invoke(usuario: Usuario) {
        if(usuario.rol.isBlank()) throw IllegalArgumentException("Rol inválido")
        if(usuario.correo.isBlank()) throw IllegalArgumentException("Correo vacío")
        if(usuario.contrasena.isBlank()) throw IllegalArgumentException("Contraseña vacía")
        if(usuario.nombres.isBlank()) throw IllegalArgumentException("Nombres vacíos")
        if(usuario.apellidoPaterno.isBlank()) throw IllegalArgumentException("Apellido paterno vacío")
        repo.insert(usuario)
    }
}
