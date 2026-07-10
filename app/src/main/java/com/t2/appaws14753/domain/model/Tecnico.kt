package com.t2.appaws14753.domain.model

data class Tecnico(
    val id: Int,
    val nombre: String,
    val email: String,
    val telefono: String,
    val especialidad: String = "General",
    val activas: Int = 0,
    val completadas: Int = 0,
    val calificacion: Double = 0.0,
    val ultimaActividad: String = "Sin actividad",
    val estado: String = "Activo"
)
