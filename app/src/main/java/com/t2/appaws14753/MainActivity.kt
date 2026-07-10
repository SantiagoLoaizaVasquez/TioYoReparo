package com.t2.appaws14753

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.t2.appaws14753.core.navigation.admin.adminGraph
import com.t2.appaws14753.core.navigation.cliente.clienteGraph
import com.t2.appaws14753.core.navigation.login.loginGraph
import com.t2.appaws14753.core.navigation.tecnico.tecnicoGraph
import com.t2.appaws14753.core.navigation.login.NavPath as LoginPath
import com.t2.appaws14753.core.navigation.cliente.NavPath as ClientePath
import com.t2.appaws14753.core.navigation.admin.NavPath as AdminPath
import com.t2.appaws14753.core.navigation.tecnico.NavPath as TecnicoPath
import com.t2.appaws14753.presentation.components.admin.AppBottomBar as AdminBottomBar
import com.t2.appaws14753.presentation.components.admin.AppTopBar as AdminTopBar
import com.t2.appaws14753.presentation.components.cliente.AppBottomBar as ClienteBottomBar
import com.t2.appaws14753.presentation.components.cliente.AppTopBar as ClienteTopBar
import com.t2.appaws14753.presentation.components.tecnico.AppBottomBar as TecnicoBottomBar
import com.t2.appaws14753.presentation.components.tecnico.AppTopBar as TecnicoTopBar
import com.t2.appaws14753.presentation.components.SnackbarEstado
import com.t2.appaws14753.presentation.components.SnackbarPersonalizado
import com.t2.appaws14753.presentation.event.EventBus
import com.t2.appaws14753.presentation.event.UiEvent
import com.t2.appaws14753.domain.model.Roles
import com.t2.appaws14753.domain.model.SesionManager
import com.t2.appaws14753.di.AppModule
import com.t2.appaws14753.ui.theme.AppAWS14753Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppAWS14753Theme {
                val navHostController = rememberNavController()
                val navBackStackEntry by navHostController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val rol = currentRoute?.substringBefore("/")
                val showBars = currentRoute != null && currentRoute != LoginPath.LOGIN

                val snackbarHostState = remember { SnackbarHostState() }

                val onLogout: () -> Unit = {
                    SesionManager.cerrarSesion()
                    navHostController.navigate(LoginPath.LOGIN) {
                        popUpTo(navHostController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }

                LaunchedEffect(Unit) {
                    AppModule.seedAdminInicial(this@MainActivity)
                }

                LaunchedEffect(Unit) {
                    EventBus.eventos.collect { event ->
                        when (event) {
                            is UiEvent.SUCCESS -> snackbarHostState.showSnackbar(
                                SnackbarPersonalizado(message = event.mensaje, estado = SnackbarEstado.SUCCESS)
                            )
                            is UiEvent.ERROR -> snackbarHostState.showSnackbar(
                                SnackbarPersonalizado(message = event.mensaje, estado = SnackbarEstado.ERROR)
                            )
                            is UiEvent.WARNING -> snackbarHostState.showSnackbar(
                                SnackbarPersonalizado(message = event.mensaje, estado = SnackbarEstado.WARNING)
                            )
                        }
                    }
                }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(snackbarHostState) { data ->
                            val personalizado = data.visuals as? SnackbarPersonalizado
                            if (personalizado != null) {
                                val estado = personalizado.estado
                                Snackbar(
                                    containerColor = estado.colorFondo,
                                    contentColor = estado.colorTexto,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = estado.icono,
                                            contentDescription = estado.descripcion,
                                            tint = estado.colorTexto
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(personalizado.message)
                                    }
                                }
                            } else {
                                Snackbar(snackbarData = data)
                            }
                        }
                    },
                    topBar = {
                        if (showBars) {
                            when (rol) {
                                "admin" -> AdminTopBar(navHostController)
                                "tecnico" -> TecnicoTopBar(navHostController)
                                else -> ClienteTopBar(navHostController)
                            }
                        }
                    },
                    bottomBar = {
                        if (showBars) {
                            when (rol) {
                                "admin" -> AdminBottomBar(navHostController)
                                "tecnico" -> TecnicoBottomBar(navHostController)
                                else -> ClienteBottomBar(navHostController)
                            }
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navHostController,
                        startDestination = LoginPath.LOGIN,
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        loginGraph(
                            onLoginSuccess = { usuario ->
                                SesionManager.iniciarSesion(usuario)
                                val destino = when (Roles.normalizar(usuario.tipo)) {
                                    Roles.ADMIN -> AdminPath.HOME
                                    Roles.TECNICO -> TecnicoPath.HOME
                                    else -> ClientePath.HOME
                                }
                                navHostController.navigate(destino) {
                                    popUpTo(LoginPath.LOGIN) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        )
                        clienteGraph(onLogout)
                        adminGraph(onLogout)
                        tecnicoGraph(onLogout)
                    }
                }
            }
        }
    }
}
