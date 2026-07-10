package com.t2.appaws14753.presentation.components.tecnico

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.t2.appaws14753.core.navigation.tecnico.ButtonNavItem

@Composable
fun AppBottomBar(navHostController: NavHostController) {
    val options = listOf(
        ButtonNavItem.Inicio,
        ButtonNavItem.Ordenes,
        ButtonNavItem.Billetera,
        ButtonNavItem.Perfil
    )

    val primaryBlue = Color(0xFF0D31B1)

    NavigationBar(
        containerColor = Color.White
    ) {
        val actualPath = navHostController.currentBackStackEntryAsState().value?.destination?.route
        options.forEach { item ->
            NavigationBarItem(
                selected = actualPath == item.path,
                onClick = {
                    if (actualPath != item.path) {
                        navHostController.navigate(item.path) {
                            popUpTo(ButtonNavItem.Inicio.path) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(item.icono, contentDescription = "") },
                label = { Text(item.tittle) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = primaryBlue,
                    selectedTextColor = primaryBlue,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
