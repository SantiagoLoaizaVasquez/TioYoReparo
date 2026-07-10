package com.t2.appaws14753.core.navigation.tecnico

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.ui.graphics.vector.ImageVector


sealed class ButtonNavItem(val path: String, val tittle: String,val icono: ImageVector) {

    data object Inicio: ButtonNavItem(path = NavPath.HOME, tittle = "Inicio",Icons.Default.Home)
    data object Ordenes: ButtonNavItem(path = NavPath.ORDER, tittle = "Ordenes",Icons.Default.Build)
    data object Billetera: ButtonNavItem(path = NavPath.WALLET, tittle = "Billetera",Icons.Default.Wallet)

}