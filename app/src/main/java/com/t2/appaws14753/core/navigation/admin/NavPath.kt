package com.t2.appaws14753.core.navigation.admin

object NavPath {
    const val HOME = "admin/Inicio"
    const val ORDER = "admin/Ordenes"
    const val DEVICES = "admin/Equipos"
    const val TECNICOS = "admin/Tecnicos"
    const val PROFILE = "admin/Perfil"




    fun getTittle(path: String?): String{
        return when{
            path == HOME -> "Bienvenido"
            path == ORDER -> "Ordenes de Servicio"
            path == DEVICES -> "Equipos del cliente"
            path == TECNICOS -> "TECNICOS"
            path == PROFILE -> "Mi Perfil"
            else -> "Bievenido"
        }
    }
}