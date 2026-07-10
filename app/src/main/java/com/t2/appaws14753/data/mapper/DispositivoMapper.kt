package com.t2.appaws14753.data.mapper

import com.t2.appaws14753.data.local.entity.DispositivoEntity
import com.t2.appaws14753.domain.model.Dispositivo

object DispositivoMapper {


    fun ToDomain(entidad: DispositivoEntity): Dispositivo=
        Dispositivo(
            dispositivoId = entidad.dispositivoId,
            clienteId = entidad.clienteId,
            marca = entidad.marca,
            modelo = entidad.modelo,
            numeroSerie = entidad.numeroSerie
        )

    fun ToEntity(dispositivo: Dispositivo): DispositivoEntity =
        DispositivoEntity(
            dispositivoId = dispositivo.dispositivoId,
            clienteId = dispositivo.clienteId,
            marca = dispositivo.marca,
            modelo = dispositivo.modelo,
            numeroSerie = dispositivo.numeroSerie
        )
}

