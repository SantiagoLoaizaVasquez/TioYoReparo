package com.t2.appaws14753.data.mapper

import com.t2.appaws14753.data.local.entity.ClienteEntity
import com.t2.appaws14753.domain.model.Cliente

fun ClienteEntity.toDomain() = Cliente(id,nombre,email,telefono)
fun Cliente.toEntity() = ClienteEntity(id,nombre,email,telefono)