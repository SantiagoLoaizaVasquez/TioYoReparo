package com.t2.appaws14753.domain.repository


interface SettingsOrdenRepository {
    suspend fun saveUltimoOrdenId(id: String)
    suspend fun getUltimoOrdenId(): String
}
