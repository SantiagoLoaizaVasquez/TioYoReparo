package com.t2.appaws14753.domain.model

import java.util.UUID

data class Dispositivo(
    val dispositivoId: String = UUID.randomUUID().toString(),
    val clienteId: String,
    val marca: String,
    val modelo: String,
    val numeroSerie: String,
)