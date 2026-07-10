package com.t2.appaws14753.domain.model

import java.util.UUID

data class Orden(
    val ordenId: String = UUID.randomUUID().toString(),
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
    val servicios: List<DetalleServicio> = emptyList()
)
