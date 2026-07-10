package com.t2.appaws14753.presentation.screen.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.QueryBuilder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.t2.appaws14753.di.AppModule
import com.t2.appaws14753.domain.model.Dispositivo
import com.t2.appaws14753.domain.model.Orden
import com.t2.appaws14753.domain.model.SesionManager
import com.t2.appaws14753.presentation.event.EventBus
import com.t2.appaws14753.presentation.event.UiEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val dispositivoUseCases = remember { AppModule.provideDispositivoUseCases(context) }
    val ordenUseCases = remember { AppModule.provideOrdenUseCases(context) }

    val primaryBlue = Color(0xFF0D31B1)
    val yellowStatus = Color(0xFFFFEB3B)
    val greenStatus = Color(0xFF4CAF50)
    val orangeStatus = Color(0xFFFF9800)

    val sesion = SesionManager.actual
    var dispositivos by remember { mutableStateOf<List<Dispositivo>>(emptyList()) }
    var ordenes by remember { mutableStateOf<List<Orden>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(sesion?.usuarioId) {
        isLoading = true
        try {
            val clienteId = sesion?.usuarioId
            if (clienteId != null) {
                dispositivos = dispositivoUseCases.getDispositivos().filter { it.clienteId == clienteId }
                ordenes = ordenUseCases.getOrdenes().filter { it.clienteId == clienteId }
            }
        } catch (e: Exception) {
            EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudo cargar tu información."))
        } finally {
            isLoading = false
        }
    }

    val equiposCount = dispositivos.size
    val ordenesActivas = ordenes.count {
        it.estado.equals("pendiente", ignoreCase = true) || it.estado.equals("en proceso", ignoreCase = true)
    }
    val reparacionesFinalizadas = ordenes.count { it.estado.equals("completado", ignoreCase = true) }
    val ordenesRecientes = ordenes.sortedByDescending { it.fechaIngreso }.take(2)

    fun equipoDe(dispositivoId: String): String =
        dispositivos.firstOrNull { it.dispositivoId == dispositivoId }?.let { "${it.marca} ${it.modelo}" } ?: "Equipo"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Bienvenido, ${sesion?.nombre ?: "Cliente"}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = primaryBlue)
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SummaryCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Inventory,
                    count = "$equiposCount",
                    label = "Mis equipos",
                    iconColor = primaryBlue
                )
                SummaryCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.QueryBuilder,
                    count = "$ordenesActivas",
                    label = "Ordenes Activas",
                    iconColor = yellowStatus
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                SummaryCard(
                    modifier = Modifier.width(160.dp),
                    icon = Icons.Default.CheckCircle,
                    count = "$reparacionesFinalizadas",
                    label = "Reparaciones Finalizadas",
                    iconColor = greenStatus
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Ordenes recientes",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (ordenesRecientes.isEmpty()) {
                        Text(
                            text = "Aún no hay órdenes registradas.",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    } else {
                        ordenesRecientes.forEachIndexed { index, orden ->
                            val statusColor = when (orden.estado.lowercase()) {
                                "completado" -> greenStatus
                                "en proceso" -> orangeStatus
                                else -> yellowStatus
                            }
                            RecentOrderItem(
                                id = orden.ordenId.take(8),
                                device = equipoDe(orden.dispositivoId),
                                date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(orden.fechaIngreso)),
                                status = orden.estado,
                                statusColor = statusColor
                            )
                            if (index != ordenesRecientes.lastIndex) {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SummaryCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    count: String,
    label: String,
    iconColor: Color
) {
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(32.dp)
            )
            Text(text = count, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun RecentOrderItem(
    id: String,
    device: String,
    date: String,
    status: String,
    statusColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9F9F9), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Orden #$id", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = "Equipo: $device", fontSize = 12.sp)
            Text(text = "Actualizado: $date", fontSize = 11.sp, color = Color.Gray)
        }
        Surface(
            color = statusColor,
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = status,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
