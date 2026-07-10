package com.t2.appaws14753.domain.repository
import com.t2.appaws14753.core.navigation.admin.ButtonNavItem
import com.t2.appaws14753.domain.model.Cliente
import com.t2.appaws14753.domain.model.Tecnico
import kotlinx.coroutines.flow.Flow

interface AdminRepository {

    fun getAll(): Flow<List<Tecnico>>
    suspend fun getById(id: Int): Cliente?
    suspend fun insert(cliente: Cliente)
    suspend fun update(cliente: Cliente)
    suspend fun delete(id: Int)
}