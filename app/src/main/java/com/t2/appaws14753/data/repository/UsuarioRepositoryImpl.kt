package com.t2.appaws14753.data.repository

import com.t2.appaws14753.data.local.dao.UsuarioDao
import com.t2.appaws14753.data.mapper.UsuarioMapper
import com.t2.appaws14753.domain.model.Usuario
import com.t2.appaws14753.domain.repository.UsuarioRepository




class UsuarioRepositoryImpl(private val dao: UsuarioDao): UsuarioRepository {

    override suspend fun insert(usuario: Usuario){
        dao.insert(UsuarioMapper.toEntity(usuario))
    }

    override suspend fun update(usuario: Usuario){
        dao.update(UsuarioMapper.toEntity(usuario))
    }

    override suspend fun delete(usuario: Usuario){
        dao.delete(usuario.usuarioId)
    }

    override suspend fun getall(): List<Usuario> =
        dao.getAll().map { UsuarioMapper.toDomain(it) }

    override suspend fun getbyid(id: String): Usuario? =
        dao.getById(id)?.let { UsuarioMapper.toDomain(it) }

}
