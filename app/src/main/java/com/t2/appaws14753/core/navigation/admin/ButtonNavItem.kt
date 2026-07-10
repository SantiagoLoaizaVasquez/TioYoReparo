package com.t2.appaws14753.core.navigation.admin

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector


sealed class ButtonNavItem(val path: String, val tittle: String,val icono: ImageVector) {

    data object Inicio: ButtonNavItem(path = NavPath.HOME, tittle = "Inicio",Icons.Default.Home)
    data object Ordenes: ButtonNavItem(path = NavPath.ORDER, tittle = "Ordenes",Icons.Default.Build)
    data object Equipos: ButtonNavItem(path = NavPath.DEVICES, tittle = "Equipos",Icons.Default.Devices)
    data object Tecnicos: ButtonNavItem(path = NavPath.TECNICOS, tittle = "Tecnicos",Icons.Default.Person)



}