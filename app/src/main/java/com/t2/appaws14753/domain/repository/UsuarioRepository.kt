package com.t2.appaws14753.domain.repository

import com.t2.appaws14753.domain.model.Usuario


interface UsuarioRepository {

    suspend fun insert(usuario: Usuario)

    suspend fun update(usuario: Usuario)

    suspend fun delete(usuario: Usuario)

    suspend fun getall(): List<Usuario>

    suspend fun getbyid(id: String): Usuario?
}