package com.t2.appaws14753.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.util.UUID


@Entity(tableName = "usuario")
data class UsuarioEntity(
    @PrimaryKey val usuarioId: String = UUID.randomUUID().toString(),
    val rol: String,
    val correo: String,
    val contrasena: String,
    val nombres: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String?,
    val especialidad: String? = null
)