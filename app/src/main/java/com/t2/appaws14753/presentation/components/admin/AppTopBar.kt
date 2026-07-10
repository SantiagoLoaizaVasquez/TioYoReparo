package com.t2.appaws14753.presentation.components.admin

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.t2.appaws14753.core.navigation.admin.NavPath

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navHostController: NavHostController) {
    val navBackStackEntry = navHostController.currentBackStackEntryAsState()
    val path = navBackStackEntry.value?.destination?.route
    val tittle = NavPath.getTittle(path)
    val showButtonBack = path != NavPath.HOME

    CenterAlignedTopAppBar(
        title = { Text(tittle) },
        navigationIcon = {
            if (showButtonBack) {
                IconButton(
                    onClick = { navHostController.popBackStack() }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        ""
                    )
                }
            }
        },
        actions = {
            if (path != NavPath.PROFILE) {
                IconButton(
                    onClick = { navHostController.navigate(NavPath.PROFILE) }
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Mi Perfil"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF3D5AFE)
        )
    )
}
