package com.t2.appaws14753.core.navigation.cliente

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.t2.appaws14753.presentation.screen.cliente.HomeScreen
import com.t2.appaws14753.presentation.screen.cliente.OrderScreen
import com.t2.appaws14753.presentation.screen.cliente.DevicesScreen

fun NavGraphBuilder.clienteGraph() {
    composable(NavPath.HOME) {
        HomeScreen()
    }
    composable(NavPath.ORDER) {
        OrderScreen()
    }
    composable(NavPath.DEVICES) {
        DevicesScreen()
    }
}
