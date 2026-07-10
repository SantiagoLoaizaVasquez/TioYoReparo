package com.t2.appaws14753.presentation.screen.tecnico

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.t2.appaws14753.di.AppModule
import com.t2.appaws14753.domain.model.Dispositivo
import com.t2.appaws14753.domain.model.Orden
import com.t2.appaws14753.domain.model.SesionManager
import com.t2.appaws14753.presentation.event.EventBus
import com.t2.appaws14753.presentation.event.UiEvent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrderScreen() {
    val context = LocalContext.current
    val ordenUseCases = remember { AppModule.provideOrdenUseCases(context) }
    val dispositivoUseCases = remember { AppModule.provideDispositivoUseCases(context) }
    val scope = rememberCoroutineScope()

    val primaryBlue = Color(0xFF0D31B1)
    val sesion = SesionManager.actual

    var ordenes by remember { mutableStateOf<List<Orden>>(emptyList()) }
    var dispositivos by remember { mutableStateOf<List<Dispositivo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var reloadTrigger by remember { mutableIntStateOf(0) }

    var showFilterMenu by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Todos los Estados") }

    var showDetailDialog by remember { mutableStateOf<Orden?>(null) }
    var showCompletarDialog by remember { mutableStateOf<Orden?>(null) }

    LaunchedEffect(reloadTrigger, sesion?.usuarioId) {
        isLoading = true
        try {
            val tecnicoId = sesion?.usuarioId
            if (tecnicoId != null) {
                ordenes = ordenUseCases.getOrdenes().filter { it.tecnicoId == tecnicoId }
                dispositivos = dispositivoUseCases.getDispositivos()
            }
        } catch (e: Exception) {
            EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudieron cargar tus órdenes."))
        } finally {
            isLoading = false
        }
    }

    fun equipoDe(dispositivoId: String): String =
        dispositivos.firstOrNull { it.dispositivoId == dispositivoId }?.let { "${it.marca} ${it.modelo} (${it.numeroSerie})" } ?: "Equipo desconocido"

    val filteredOrders = ordenes
        .filter { selectedFilter == "Todos los Estados" || it.estado.equals(selectedFilter, ignoreCase = true) }
        .sortedByDescending { it.fechaIngreso }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Ordenes de Servicio",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Gestiona las reparaciones asignadas a ti",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedFilter,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth().clickable { showFilterMenu = true },
                leadingIcon = { Icon(Icons.Default.FilterAlt, null) },
                shape = RoundedCornerShape(50.dp),
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color.LightGray,
                    disabledTextColor = Color.Black,
                    disabledLeadingIconColor = primaryBlue
                )
            )
            Box(modifier = Modifier.matchParentSize().clickable { showFilterMenu = true })

            DropdownMenu(
                expanded = showFilterMenu,
                onDismissRequest = { showFilterMenu = false }
            ) {
                DropdownMenuItem(text = { Text("Todos los Estados") }, onClick = { selectedFilter = "Todos los Estados"; showFilterMenu = false })
                DropdownMenuItem(text = { Text("Completado") }, onClick = { selectedFilter = "completado"; showFilterMenu = false })
                DropdownMenuItem(text = { Text("Pendiente") }, onClick = { selectedFilter = "pendiente"; showFilterMenu = false })
                DropdownMenuItem(text = { Text("En Proceso") }, onClick = { selectedFilter = "en proceso"; showFilterMenu = false })
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryBlue)
                }
            }
            filteredOrders.isEmpty() -> {
                Text("No tienes órdenes asignadas.", fontSize = 13.sp, color = Color.Gray)
            }
            else -> {
                filteredOrders.forEach { orden ->
                    DetailedOrderCard(
                        orden = orden,
                        equipoNombre = equipoDe(orden.dispositivoId),
                        onViewDetail = { showDetailDialog = orden },
                        onMarcarTerminado = { showCompletarDialog = orden }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    showDetailDialog?.let { orden ->
        OrderDetailDialog(orden = orden, equipoNombre = equipoDe(orden.dispositivoId), onDismiss = { showDetailDialog = null })
    }

    showCompletarDialog?.let { orden ->
        CompletarOrdenDialog(
            orden = orden,
            onDismiss = { showCompletarDialog = null },
            onConfirmar = { costo ->
                scope.launch {
                    try {
                        ordenUseCases.actualizarOrden(
                            orden.copy(
                                estado = "completado",
                                totalCobrado = costo,
                                fechaEntrega = System.currentTimeMillis()
                            )
                        )
                        showCompletarDialog = null
                        reloadTrigger++
                        EventBus.enviar(
                            UiEvent.SUCCESS("Orden #${orden.ordenId.take(8)} marcada como terminada. Se sumó S/${"%.2f".format(costo)} a tu billetera.")
                        )
                    } catch (e: Exception) {
                        EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudo actualizar la orden."))
                    }
                }
            }
        )
    }
}

private fun formatFecha(timestamp: Long?): String {
    if (timestamp == null) return "-"
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Composable
fun DetailedOrderCard(
    orden: Orden,
    equipoNombre: String,
    onViewDetail: () -> Unit,
    onMarcarTerminado: () -> Unit
) {
    val yellowStatus = Color(0xFFFFEB3B)
    val greenStatus = Color(0xFF4CAF50)
    val orangeStatus = Color(0xFFFF9800)

    val redPriority = Color(0xFFFF8A80)
    val greenPriority = Color(0xFFB9F6CA)
    val yellowPriority = Color(0xFFFFF59D)

    val statusColor = when (orden.estado.lowercase()) {
        "completado" -> greenStatus
        "en proceso" -> orangeStatus
        else -> yellowStatus
    }

    val priorityColor = when (orden.prioridad.lowercase()) {
        "alta" -> redPriority
        "media" -> yellowPriority
        else -> greenPriority
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = "Orden #${orden.ordenId.take(8)}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = formatFecha(orden.fechaIngreso), fontSize = 12.sp, color = Color.Gray)
                }
                Surface(color = statusColor, shape = RoundedCornerShape(12.dp)) {
                    Text(
                        text = orden.estado,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE0F2F1), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(text = "Equipo", fontSize = 11.sp, color = Color.Gray)
                Text(text = equipoNombre, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(color = priorityColor, shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "Prioridad ${orden.prioridad}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                TextButton(onClick = onViewDetail) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color(0xFFB06AB3),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ver Detalle", color = Color(0xFFB06AB3), fontSize = 13.sp)
                    }
                }
            }

            if (!orden.estado.equals("completado", ignoreCase = true)) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onMarcarTerminado,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Marcar Terminado", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun OrderDetailDialog(orden: Orden, equipoNombre: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Detalle de Orden #${orden.ordenId.take(8)}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DetailItem("Equipo:", equipoNombre)
                DetailItem("Fecha ingreso:", formatFecha(orden.fechaIngreso))
                DetailItem("Estado:", orden.estado)
                DetailItem("Prioridad:", orden.prioridad)
                DetailItem("Diagnóstico:", orden.detalleDiagnostico)
                DetailItem("Fecha entrega:", formatFecha(orden.fechaEntrega))
                if (orden.totalCobrado > 0) {
                    DetailItem("Costo:", "S/${"%.2f".format(orden.totalCobrado)}")
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { Text("Cerrar") }
        }
    )
}

@Composable
fun DetailItem(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
        Text(text = value)
    }
}

@Composable
fun CompletarOrdenDialog(
    orden: Orden,
    onDismiss: () -> Unit,
    onConfirmar: (costo: Double) -> Unit
) {
    var costoTexto by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Marcar Orden #${orden.ordenId.take(8)} como Terminada") },
        text = {
            Column {
                Text(
                    "Ingresa el costo del servicio. Este monto se sumará automáticamente a tu billetera.",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = costoTexto,
                    onValueChange = {
                        costoTexto = it
                        errorMsg = ""
                    },
                    label = { Text("Costo del servicio (S/)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val costo = costoTexto.trim().replace(",", ".").toDoubleOrNull()
                    if (costo == null || costo <= 0.0) {
                        errorMsg = "Ingresa un costo válido mayor a 0."
                        return@Button
                    }
                    onConfirmar(costo)
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
