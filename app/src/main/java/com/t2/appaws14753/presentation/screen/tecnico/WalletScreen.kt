package com.t2.appaws14753.presentation.screen.tecnico

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.t2.appaws14753.di.AppModule
import com.t2.appaws14753.domain.model.COMISION_TECNICO
import com.t2.appaws14753.domain.model.Dispositivo
import com.t2.appaws14753.domain.model.Orden
import com.t2.appaws14753.domain.model.SesionManager
import com.t2.appaws14753.presentation.event.EventBus
import com.t2.appaws14753.presentation.event.UiEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WalletScreen() {
    val context = LocalContext.current
    val ordenUseCases = remember { AppModule.provideOrdenUseCases(context) }
    val dispositivoUseCases = remember { AppModule.provideDispositivoUseCases(context) }

    val primaryBlue = Color(0xFF0D31B1)
    val lightBlue = Color(0xFF2196F3)

    val sesion = SesionManager.actual
    var ordenes by remember { mutableStateOf<List<Orden>>(emptyList()) }
    var dispositivos by remember { mutableStateOf<List<Dispositivo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var showDetailDialog by remember { mutableStateOf<Orden?>(null) }

    LaunchedEffect(sesion?.usuarioId) {
        isLoading = true
        try {
            val tecnicoId = sesion?.usuarioId
            if (tecnicoId != null) {
                ordenes = ordenUseCases.getOrdenes().filter { it.tecnicoId == tecnicoId }
                dispositivos = dispositivoUseCases.getDispositivos()
            }
        } catch (e: Exception) {
            EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudo cargar tu billetera."))
        } finally {
            isLoading = false
        }
    }

    fun equipoDe(dispositivoId: String): String =
        dispositivos.firstOrNull { it.dispositivoId == dispositivoId }?.let { "${it.marca} ${it.modelo}" } ?: "Equipo desconocido"

    val ordenesPagadas = ordenes
        .filter { it.estado.equals("completado", ignoreCase = true) && it.totalCobrado > 0.0 }
        .sortedBy { it.fechaIngreso }

    val totalGenerado = ordenesPagadas.sumOf { it.totalCobrado * COMISION_TECNICO }
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val fechaInicio = ordenesPagadas.minByOrNull { it.fechaIngreso }?.let { sdf.format(Date(it.fechaIngreso)) } ?: "-"
    val fechaFin = ordenesPagadas
        .mapNotNull { it.fechaEntrega ?: it.fechaIngreso }
        .maxOrNull()?.let { sdf.format(Date(it)) } ?: "-"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Gestión de Billetera",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Ingresos generados por órdenes completadas",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    Brush.horizontalGradient(listOf(lightBlue, primaryBlue)),
                    RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "S/${"%.2f".format(totalGenerado)}",
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Generados del $fechaInicio al $fechaFin",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "Tu comisión es el ${(COMISION_TECNICO * 100).toInt()}% del monto de cada orden",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryBlue)
                }
            }
            ordenesPagadas.isEmpty() -> {
                Text(
                    text = "Aún no hay órdenes pagadas para mostrar.",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
            else -> {
                ordenesPagadas.forEach { orden ->
                    WalletOrderItem(
                        orden = orden,
                        primaryBlue = primaryBlue,
                        onViewDetail = { showDetailDialog = orden }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    showDetailDialog?.let { orden ->
        WalletOrderDetailDialog(
            orden = orden,
            equipoNombre = equipoDe(orden.dispositivoId),
            primaryBlue = primaryBlue,
            onDismiss = { showDetailDialog = null }
        )
    }
}

@Composable
fun WalletOrderItem(
    orden: Orden,
    primaryBlue: Color,
    onViewDetail: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF0F0F0)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Orden #${orden.ordenId.take(8)}:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "S/${"%.2f".format(orden.totalCobrado * COMISION_TECNICO)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Ver detalle",
                    tint = primaryBlue,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onViewDetail() }
                )
            }
        }
    }
}

@Composable
fun WalletOrderDetailDialog(orden: Orden, equipoNombre: String, primaryBlue: Color, onDismiss: () -> Unit) {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Orden #${orden.ordenId.take(8)}") },
        text = {
            Column {
                DetailRow("Equipo:", equipoNombre)
                DetailRow("Diagnóstico:", orden.detalleDiagnostico)
                DetailRow("Fecha ingreso:", sdf.format(Date(orden.fechaIngreso)))
                DetailRow("Fecha completado:", orden.fechaEntrega?.let { sdf.format(Date(it)) } ?: "-")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Monto cobrado al cliente: S/${"%.2f".format(orden.totalCobrado)}",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "Tu comisión (${(COMISION_TECNICO * 100).toInt()}%): S/${"%.2f".format(orden.totalCobrado * COMISION_TECNICO)}",
                    fontWeight = FontWeight.Bold,
                    color = primaryBlue,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        },
        confirmButton = { Button(onClick = onDismiss) { Text("Cerrar") } }
    )
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(text = "$label ", fontSize = 13.sp, fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = 13.sp, color = Color.DarkGray)
    }
}
