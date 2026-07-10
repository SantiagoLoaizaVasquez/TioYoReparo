package com.t2.appaws14753.presentation.components.cliente

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.t2.appaws14753.core.navigation.cliente.NavPath

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navHostController: NavHostController){
    val navBackStackEntry= navHostController.currentBackStackEntryAsState()
    val path = navBackStackEntry.value?.destination?.route
    val tittle = NavPath.getTittle(path)
    val showButtonBack = path != NavPath.HOME

    CenterAlignedTopAppBar(
        title = {Text(tittle)},
        navigationIcon = {
            if(showButtonBack){
                IconButton(
                    onClick = {navHostController.popBackStack()}
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        ""
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF3D5AFE)
        )
    )
}