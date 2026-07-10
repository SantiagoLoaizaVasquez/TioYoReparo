package com.t2.appaws14753.data.mapper

import com.t2.appaws14753.data.local.entity.ServicioEntity
import com.t2.appaws14753.domain.model.Servicio

object ServicioMapper {


    fun toDomain(entidad: ServicioEntity): Servicio =
        Servicio(
            servicioId = entidad.servicioId,
            nombreServicio = entidad.nombreServicio,
            precioServicio = entidad.precioServicio
        )


    fun toEntity(servicio: Servicio): ServicioEntity =
        ServicioEntity(
            servicioId = servicio.servicioId,
            nombreServicio = servicio.nombreServicio,
            precioServicio = servicio.precioServicio
        )
}

