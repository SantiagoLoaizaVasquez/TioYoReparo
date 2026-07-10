package com.t2.appaws14753.domain.repository


interface SettingsClienteRepository {
    suspend fun saveUltimoClienteId(id: String)
    suspend fun getUltimoClienteId(): String
}
