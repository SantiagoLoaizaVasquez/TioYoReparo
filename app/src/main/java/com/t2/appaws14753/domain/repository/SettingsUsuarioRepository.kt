package com.t2.appaws14753.domain.repository


interface SettingsUsuarioRepository {
    suspend fun saveUsuarioRol(rol: String)
    suspend fun getUsuarioRol(): String
}