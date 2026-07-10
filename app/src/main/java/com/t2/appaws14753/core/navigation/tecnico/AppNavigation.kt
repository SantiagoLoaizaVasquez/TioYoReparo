package com.t2.appaws14753.core.navigation.tecnico

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.t2.appaws14753.core.navigation.tecnico.NavPath
import com.t2.appaws14753.presentation.screen.tecnico.HomeScreen
import com.t2.appaws14753.presentation.screen.tecnico.OrderScreen
import com.t2.appaws14753.presentation.screen.tecnico.WalletScreen

fun NavGraphBuilder.tecnicoGraph() {
    composable(NavPath.HOME) {
        HomeScreen()
    }
    composable(NavPath.ORDER) {
        OrderScreen()
    }
    composable(NavPath.WALLET) {
        WalletScreen()
    }
}
