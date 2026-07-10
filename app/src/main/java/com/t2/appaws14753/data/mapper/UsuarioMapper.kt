package com.t2.appaws14753.data.mapper

import com.t2.appaws14753.data.local.entity.UsuarioEntity
import com.t2.appaws14753.domain.model.Usuario

object UsuarioMapper {

    fun toDomain(entidad: UsuarioEntity): Usuario =
        Usuario(
            usuarioId       = entidad.usuarioId,
            rol             = entidad.rol,
            correo          = entidad.correo,
            contrasena      = entidad.contrasena,
            nombres         = entidad.nombres,
            apellidoPaterno = entidad.apellidoPaterno,
            apellidoMaterno = entidad.apellidoMaterno,
            especialidad    = entidad.especialidad
        )

    fun toEntity(usuario: Usuario): UsuarioEntity =
        UsuarioEntity(
            usuarioId       = usuario.usuarioId,
            rol             = usuario.rol,
            correo          = usuario.correo,
            contrasena      = usuario.contrasena,
            nombres         = usuario.nombres,
            apellidoPaterno = usuario.apellidoPaterno,
            apellidoMaterno = usuario.apellidoMaterno,
            especialidad    = usuario.especialidad
        )
}