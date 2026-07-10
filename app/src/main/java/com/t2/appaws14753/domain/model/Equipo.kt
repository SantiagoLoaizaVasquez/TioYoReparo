package com.t2.appaws14753.domain.model

data class Equipo(
    val id: Int,
    val nombre: String,
    val marca: String,
    val nroSerie: String,
    val garantiaHasta: String,
    val ultimoManto: String,
    val estado: String,
    val historial: List<ReparacionHistorica> = emptyList()
)
