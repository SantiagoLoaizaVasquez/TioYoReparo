package com.t2.appaws14753.domain.repository


interface SettingsServicioRepository {
    suspend fun saveUltimoServicioId(id: String)
    suspend fun getUltimoServicioId(): String
}
