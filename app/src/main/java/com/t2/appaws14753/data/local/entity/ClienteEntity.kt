package com.t2.appaws14753.data.local.entity
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.util.UUID

@Entity(tableName = "clientes")
data class ClienteEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val email: String,
    val telefono: String
)