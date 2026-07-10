package com.t2.appaws14753.core.navigation.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.t2.appaws14753.presentation.screen.admin.HomeScreen
import com.t2.appaws14753.presentation.screen.admin.OrderScreen
import com.t2.appaws14753.presentation.screen.admin.DevicesScreen
import com.t2.appaws14753.presentation.screen.admin.ProfileScreen
import com.t2.appaws14753.presentation.screen.admin.UsuariosScreen
import com.t2.appaws14753.presentation.screen.admin.IngresosScreen

fun NavGraphBuilder.adminGraph(onLogout: () -> Unit) {
    composable(NavPath.HOME) {
        HomeScreen()
    }
    composable(NavPath.ORDER) {
        OrderScreen()
    }
    composable(NavPath.DEVICES) {
        DevicesScreen()
    }
    composable(NavPath.PROFILE) {
        ProfileScreen(onLogout = onLogout)
    }
    composable(NavPath.USUARIOS) {
        UsuariosScreen()
    }
    composable(NavPath.INGRESOS) {
        IngresosScreen()
    }
}
