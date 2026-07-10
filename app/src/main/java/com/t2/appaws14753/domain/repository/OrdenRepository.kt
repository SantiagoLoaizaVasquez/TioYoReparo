package com.t2.appaws14753.domain.repository

import com.t2.appaws14753.domain.model.Orden


interface OrdenRepository {

    suspend fun getAll(): List<Orden>

    suspend fun getById(id: String): Orden?

    suspend fun insert(orden: Orden)

    suspend fun update(orden: Orden)

    suspend fun delete(orden: Orden)
}
