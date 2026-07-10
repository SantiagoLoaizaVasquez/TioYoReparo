package com.t2.appaws14753.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.t2.appaws14753.data.local.entity.OrdenEntity
import com.t2.appaws14753.domain.model.DetalleServicio
import com.t2.appaws14753.domain.model.Orden


object OrdenMapper {

    private val gson = Gson()
    private val tipoListaServicios = object : TypeToken<List<DetalleServicio>>() {}.type

    fun toDomain(entidad: OrdenEntity): Orden =
        Orden(
            ordenId = entidad.ordenId,
            dispositivoId = entidad.dispositivoId,
            clienteId = entidad.clienteId,
            tecnicoId = entidad.tecnicoId,
            tecnicoNombre = entidad.tecnicoNombre,
            estado = entidad.estado,
            prioridad= entidad.prioridad,
            fechaIngreso = entidad.fechaIngreso,
            fechaEntrega = entidad.fechaEntrega,
            detalleDiagnostico = entidad.detalleDiagnostico,
            totalCobrado = entidad.totalCobrado,
            servicios = gson.fromJson(entidad.servicios, tipoListaServicios) ?: emptyList()
        )

    fun toEntity(orden: Orden): OrdenEntity =
        OrdenEntity(
            ordenId = orden.ordenId,
            dispositivoId = orden.dispositivoId,
            clienteId = orden.clienteId,
            tecnicoId = orden.tecnicoId,
            tecnicoNombre = orden.tecnicoNombre,
            estado = orden.estado,
            prioridad= orden.prioridad,
            fechaIngreso = orden.fechaIngreso,
            fechaEntrega = orden.fechaEntrega,
            detalleDiagnostico = orden.detalleDiagnostico,
            totalCobrado = orden.totalCobrado,
            servicios = gson.toJson(orden.servicios)

        )


}

