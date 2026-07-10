package com.t2.appaws14753.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.util.UUID

@Entity(tableName = "dispositivo")
data class DispositivoEntity(
    @PrimaryKey val dispositivoId: String = UUID.randomUUID().toString(),
    val clienteId: String,
    val marca: String,
    val modelo: String,
    val numeroSerie: String
)