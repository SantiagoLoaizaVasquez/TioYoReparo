package com.t2.appaws14753.core.navigation.admin

object NavPath {
    const val HOME = "admin/Inicio"
    const val ORDER = "admin/Ordenes"
    const val DEVICES = "admin/Equipos"
    const val PROFILE = "admin/Perfil"
    const val USUARIOS = "admin/Usuarios"
    const val INGRESOS = "admin/Ingresos"




    fun getTittle(path: String?): String{
        return when{
            path == HOME -> "Bienvenido"
            path == ORDER -> "Ordenes de Servicio"
            path == DEVICES -> "Equipos del cliente"
            path == PROFILE -> "Mi Perfil"
            path == USUARIOS -> "Gestión de Usuarios"
            path == INGRESOS -> "Ingresos del Negocio"
            else -> "Bievenido"
        }
    }
}