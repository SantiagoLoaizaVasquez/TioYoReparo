package com.t2.appaws14753.data.local.entity


import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

import java.util.UUID

@Entity(tableName = "orden")
data class OrdenEntity(
    @PrimaryKey val ordenId: String = UUID.randomUUID().toString(),
    val dispositivoId: String,
    val clienteId: String,
    val tecnicoId: String,
    val tecnicoNombre: String,
    val estado: String,
    val prioridad: String,
    val fechaIngreso: Long,
    val fechaEntrega: Long? = null,
    val detalleDiagnostico: String,
    val totalCobrado: Double,
    val servicios: String
)