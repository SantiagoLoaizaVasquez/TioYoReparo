package com.t2.appaws14753.core.navigation.tecnico

import androidx.compose.ui.tooling.preview.Devices

object NavPath {

    const val HOME = "tecnico/Inicio"
    const val ORDER = "tecnico/Ordenes"
    const val WALLET = "tecnico/Billetera"


    fun getTittle(path: String?): String{
        return when{
            path == HOME -> "Bienvenido"
            path == ORDER -> "Ordenes de Servicio"
            path == WALLET -> "Billetera cliente"
            else -> "Bievenido"
        }
    }
}