package com.t2.appaws14753.domain.model

data class OrdenServicio(
    val numero: Int,
    val tipo: String,
    val equipo: String,
    val nroSerie: String = "",
    val estado: String,
    val prioridad: String,
    val actualizado: String,
    val tecnico: String = "Por asignar",
    val fechaCreacion: String = "",
    val fechaGarantia: String = "-",
    val fechaCompletado: String = "-",
    val costo: Double = 0.0
)
