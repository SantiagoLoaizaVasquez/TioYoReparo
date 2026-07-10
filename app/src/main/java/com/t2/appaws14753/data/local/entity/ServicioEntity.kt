package com.t2.appaws14753.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.util.UUID


@Entity(tableName = "servicio")
data class ServicioEntity(
    @PrimaryKey val servicioId: String = UUID.randomUUID().toString(),
    val nombreServicio: String,
    val precioServicio: Double
)