package com.t2.appaws14753.presentation.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.t2.appaws14753.di.AppModule
import com.t2.appaws14753.domain.model.Orden
import com.t2.appaws14753.domain.model.Roles
import com.t2.appaws14753.domain.model.Usuario
import com.t2.appaws14753.presentation.event.EventBus
import com.t2.appaws14753.presentation.event.UiEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TecnicosScreen() {

    val context = LocalContext.current
    val usuarioUseCases = remember { AppModule.provideUsuarioUseCases(context) }
    val ordenUseCases = remember { AppModule.provideOrdenUseCases(context) }

    val primaryBlue = Color(0xFF0D31B1)
    val yellow = Color(0xFFFFEB3B)
    val green = Color(0xFF4CAF50)

    var tecnicos by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    var ordenes by remember { mutableStateOf<List<Orden>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val usuarios = usuarioUseCases.getUsuarios()
            tecnicos = usuarios.filter { Roles.normalizar(it.rol) == Roles.TECNICO }
            ordenes = ordenUseCases.getOrdenes()
        } catch (e: Exception) {
            EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudo cargar la información de técnicos."))
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Default.Build,
            contentDescription = null,
            tint = primaryBlue,
            modifier = Modifier.size(45.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Desempeño de Técnicos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Órdenes activas y completadas por técnico",
            color = Color.Gray,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Para añadir o editar técnicos, ve a la pestaña Usuarios.",
            color = Color.Gray,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryBlue)
                }
            }
            tecnicos.isEmpty() -> {
                Text(
                    text = "Aún no hay técnicos registrados.",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            else -> {
                tecnicos.forEach { tecnico ->
                    val ordenesTecnico = ordenes.filter { it.tecnicoId == tecnico.usuarioId }
                    val activas = ordenesTecnico.count {
                        it.estado.equals("pendiente", ignoreCase = true) || it.estado.equals("en proceso", ignoreCase = true)
                    }
                    val completadas = ordenesTecnico.count { it.estado.equals("completado", ignoreCase = true) }
                    val ultimaActividad = ordenesTecnico
                        .mapNotNull { it.fechaEntrega ?: it.fechaIngreso }
                        .maxOrNull()

                    TecnicoItemCard(
                        tecnico = tecnico,
                        activas = activas,
                        completadas = completadas,
                        ultimaActividad = formatFecha(ultimaActividad),
                        primaryBlue = primaryBlue,
                        yellow = yellow,
                        green = green
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

private fun formatFecha(timestamp: Long?): String {
    if (timestamp == null) return "Sin actividad"
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Composable
fun TecnicoItemCard(
    tecnico: Usuario,
    activas: Int,
    completadas: Int,
    ultimaActividad: String,
    primaryBlue: Color,
    yellow: Color,
    green: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(70.dp),
                tint = primaryBlue
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${tecnico.nombres} ${tecnico.apellidoPaterno}".uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Text(
                text = tecnico.especialidad?.takeIf { it.isNotBlank() } ?: "Sin especialidad",
                color = Color.Gray
            )

            Text(
                text = tecnico.correo,
                color = Color.Gray,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Icon(
                        Icons.Default.Refresh,
                        null,
                        tint = yellow
                    )

                    Text(
                        "$activas",
                        fontWeight = FontWeight.Bold
                    )

                    Text("Activas")
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Icon(
                        Icons.Default.CheckCircle,
                        null,
                        tint = green
                    )

                    Text(
                        "$completadas",
                        fontWeight = FontWeight.Bold
                    )

                    Text("Completadas")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Última actividad: $ultimaActividad",
                color = Color.Gray
            )
        }
    }
}
