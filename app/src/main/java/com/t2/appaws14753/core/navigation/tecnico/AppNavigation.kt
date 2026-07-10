package com.t2.appaws14753.core.navigation.tecnico

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.t2.appaws14753.presentation.screen.tecnico.HomeScreen
import com.t2.appaws14753.presentation.screen.tecnico.OrderScreen
import com.t2.appaws14753.presentation.screen.tecnico.WalletScreen
import com.t2.appaws14753.presentation.screen.tecnico.ProfileScreen

fun NavGraphBuilder.tecnicoGraph(onLogout: () -> Unit) {
    composable(NavPath.HOME) {
        HomeScreen()
    }
    composable(NavPath.ORDER) {
        OrderScreen()
    }
    composable(NavPath.WALLET) {
        WalletScreen()
    }
    composable(NavPath.PROFILE) {
        ProfileScreen(onLogout = onLogout)
    }
}
