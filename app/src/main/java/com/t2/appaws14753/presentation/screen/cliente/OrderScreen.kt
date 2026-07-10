package com.t2.appaws14753.presentation.screen.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Visibility
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

    var showNewOrderDialog by remember { mutableStateOf(false) }
    var showDetailDialog by remember { mutableStateOf<Orden?>(null) }

    LaunchedEffect(reloadTrigger, sesion?.usuarioId) {
        isLoading = true
        try {
            val clienteId = sesion?.usuarioId
            if (clienteId != null) {
                ordenes = ordenUseCases.getOrdenes().filter { it.clienteId == clienteId }
                dispositivos = dispositivoUseCases.getDispositivos().filter { it.clienteId == clienteId }
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
            text = "Tus reparaciones y mantenimientos",
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showNewOrderDialog = true },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
            enabled = dispositivos.isNotEmpty()
        ) {
            Text("Nueva Orden", fontWeight = FontWeight.SemiBold)
        }
        if (dispositivos.isEmpty() && !isLoading) {
            Text(
                "Registra un equipo primero en la pestaña Equipos.",
                color = Color.Gray,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryBlue)
                }
            }
            filteredOrders.isEmpty() -> {
                Text("No hay órdenes registradas.", fontSize = 13.sp, color = Color.Gray)
            }
            else -> {
                filteredOrders.forEach { orden ->
                    DetailedOrderCard(
                        orden = orden,
                        equipoNombre = equipoDe(orden.dispositivoId),
                        onViewDetail = { showDetailDialog = orden }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showNewOrderDialog) {
        NewOrderDialog(
            dispositivos = dispositivos,
            onDismiss = { showNewOrderDialog = false },
            onConfirmar = { dispositivo, motivo ->
                val clienteId = sesion?.usuarioId
                if (clienteId != null) {
                    scope.launch {
                        try {
                            ordenUseCases.insertarOrden(
                                Orden(
                                    dispositivoId = dispositivo.dispositivoId,
                                    clienteId = clienteId,
                                    tecnicoId = "",
                                    tecnicoNombre = "Sin asignar",
                                    estado = "pendiente",
                                    prioridad = "Baja",
                                    fechaIngreso = System.currentTimeMillis(),
                                    detalleDiagnostico = motivo,
                                    totalCobrado = 0.0
                                )
                            )
                            EventBus.enviar(UiEvent.SUCCESS("Orden creada correctamente."))
                            showNewOrderDialog = false
                            reloadTrigger++
                        } catch (e: Exception) {
                            EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudo crear la orden."))
                        }
                    }
                }
            }
        )
    }

    showDetailDialog?.let { orden ->
        OrderDetailDialog(orden = orden, equipoNombre = equipoDe(orden.dispositivoId), onDismiss = { showDetailDialog = null })
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
    onViewDetail: () -> Unit
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
                        Icon(Icons.Default.Visibility, contentDescription = null, tint = Color(0xFFB06AB3), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ver Detalle", color = Color(0xFFB06AB3), fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewOrderDialog(dispositivos: List<Dispositivo>, onDismiss: () -> Unit, onConfirmar: (Dispositivo, String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedDispositivo by remember { mutableStateOf<Dispositivo?>(null) }
    var motivo by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Orden") },
        text = {
            Column {
                Text("Seleccionar Equipo Registrado", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedDispositivo?.let { "${it.marca} ${it.modelo}" } ?: "Seleccionar...",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.Gray
                        ),
                        enabled = false
                    )
                    Box(modifier = Modifier.matchParentSize().clickable { expanded = true })

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        dispositivos.forEach { dispositivo ->
                            DropdownMenuItem(
                                text = { Text("${dispositivo.marca} ${dispositivo.modelo} (${dispositivo.numeroSerie})") },
                                onClick = {
                                    selectedDispositivo = dispositivo
                                    expanded = false
                                    errorMsg = ""
                                }
                            )
                        }
                    }
                }

                if (dispositivos.isEmpty()) {
                    Text(
                        "No tienes equipos registrados. Regístralos primero en la pestaña Equipos.",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = motivo,
                    onValueChange = { motivo = it; errorMsg = "" },
                    label = { Text("Motivo de la solicitud") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val dispositivo = selectedDispositivo
                    if (dispositivo == null) {
                        errorMsg = "Debes seleccionar un equipo"
                        return@Button
                    }
                    if (motivo.isBlank()) {
                        errorMsg = "Describe el motivo de la solicitud"
                        return@Button
                    }
                    onConfirmar(dispositivo, motivo.trim())
                },
                enabled = selectedDispositivo != null
            ) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun OrderDetailDialog(orden: Orden, equipoNombre: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Detalle de Orden #${orden.ordenId.take(8)}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DetailItem("Equipo:", equipoNombre)
                DetailItem("Técnico:", orden.tecnicoNombre.ifBlank { "Sin asignar" })
                DetailItem("Fecha ingreso:", formatFecha(orden.fechaIngreso))
                DetailItem("Estado:", orden.estado)
                DetailItem("Fecha entrega:", formatFecha(orden.fechaEntrega))
                DetailItem("Diagnóstico:", orden.detalleDiagnostico)
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
