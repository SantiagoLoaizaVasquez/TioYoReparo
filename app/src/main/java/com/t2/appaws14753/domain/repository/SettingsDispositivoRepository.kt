package com.t2.appaws14753.domain.repository


interface SettingsDispositivoRepository {
    suspend fun saveUltimoDispositivoId(id: String)
    suspend fun getUltimoDispositivoId(): String
}
