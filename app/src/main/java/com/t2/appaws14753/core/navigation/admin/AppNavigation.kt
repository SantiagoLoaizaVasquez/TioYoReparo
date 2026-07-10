package com.t2.appaws14753.core.navigation.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.t2.appaws14753.presentation.screen.admin.HomeScreen
import com.t2.appaws14753.presentation.screen.admin.OrderScreen
import com.t2.appaws14753.presentation.screen.admin.DevicesScreen
import com.t2.appaws14753.presentation.screen.admin.TecnicosScreen
import com.t2.appaws14753.presentation.screen.admin.ProfileScreen

fun NavGraphBuilder.adminGraph() {
    composable(NavPath.HOME) {
        HomeScreen()
    }
    composable(NavPath.ORDER) {
        OrderScreen()
    }
    composable(NavPath.DEVICES) {
        DevicesScreen()
    }
    composable(NavPath.TECNICOS) {
        TecnicosScreen()
    }
    composable(NavPath.PROFILE) {
        ProfileScreen()
    }
}
