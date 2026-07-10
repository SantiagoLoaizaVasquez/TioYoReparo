package com.t2.appaws14753.domain.model

import java.util.UUID

data class Servicio(
    val servicioId: String = UUID.randomUUID().toString(),
    val nombreServicio: String,
    val precioServicio: Double
)