package com.t2.appaws14753.core.navigation.cliente

object NavPath {
    const val HOME = "cliente/Inicio"
    const val ORDER = "cliente/Ordenes"
    const val DEVICES = "cliente/Equipos"


    fun getTittle(path: String?): String{
        return when{
            path == HOME -> "Bienvenido"
            path == ORDER -> "Ordenes de Servicio"
            path == DEVICES -> "Inventario de Equipos"
            else -> "Bievenido"
        }
    }
}