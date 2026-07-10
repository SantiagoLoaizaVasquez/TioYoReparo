package com.t2.appaws14753.presentation.screen.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Laptop
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
fun DevicesScreen() {
    val context = LocalContext.current
    val dispositivoUseCases = remember { AppModule.provideDispositivoUseCases(context) }
    val ordenUseCases = remember { AppModule.provideOrdenUseCases(context) }
    val scope = rememberCoroutineScope()

    val primaryBlue = Color(0xFF0D31B1)
    val lightBlue = Color(0xFF2196F3)

    val sesion = SesionManager.actual
    var dispositivos by remember { mutableStateOf<List<Dispositivo>>(emptyList()) }
    var ordenes by remember { mutableStateOf<List<Orden>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var reloadTrigger by remember { mutableIntStateOf(0) }

    var showAddDialog by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf<Dispositivo?>(null) }
    var showDeleteDialog by remember { mutableStateOf<Dispositivo?>(null) }

    LaunchedEffect(reloadTrigger, sesion?.usuarioId) {
        isLoading = true
        try {
            val clienteId = sesion?.usuarioId
            if (clienteId != null) {
                dispositivos = dispositivoUseCases.getDispositivos().filter { it.clienteId == clienteId }
                ordenes = ordenUseCases.getOrdenes().filter { it.clienteId == clienteId }
            }
        } catch (e: Exception) {
            EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudieron cargar tus equipos."))
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Mis Equipos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Tus equipos registrados",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
            enabled = sesion?.usuarioId != null
        ) {
            Icon(Icons.Default.Add, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Registrar Equipo", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryBlue)
                }
            }
            dispositivos.isEmpty() -> {
                Text(text = "No hay equipos registrados aún.", fontSize = 13.sp, color = Color.Gray)
            }
            else -> {
                Text(
                    text = "${dispositivos.size} equipo(s) registrado(s)",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                dispositivos.forEach { dispositivo ->
                    DeviceItemCard(
                        dispositivo = dispositivo,
                        primaryBlue = primaryBlue,
                        lightBlue = lightBlue,
                        onViewHistory = { showHistoryDialog = dispositivo },
                        onDelete = { showDeleteDialog = dispositivo }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showAddDialog) {
        AddEquipmentDialog(
            onDismiss = { showAddDialog = false },
            onConfirmar = { marca, modelo, serial ->
                val clienteId = sesion?.usuarioId
                if (clienteId != null) {
                    scope.launch {
                        try {
                            dispositivoUseCases.insertarDispositivo(
                                Dispositivo(clienteId = clienteId, marca = marca, modelo = modelo, numeroSerie = serial)
                            )
                            EventBus.enviar(UiEvent.SUCCESS("Equipo registrado correctamente."))
                            showAddDialog = false
                            reloadTrigger++
                        } catch (e: Exception) {
                            EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudo registrar el equipo."))
                        }
                    }
                }
            }
        )
    }

    showHistoryDialog?.let { dispositivo ->
        HistoryDialog(
            dispositivo = dispositivo,
            ordenes = ordenes.filter { it.dispositivoId == dispositivo.dispositivoId },
            onDismiss = { showHistoryDialog = null }
        )
    }

    showDeleteDialog?.let { dispositivo ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Eliminar equipo") },
            text = { Text("¿Seguro que deseas eliminar \"${dispositivo.marca} ${dispositivo.modelo}\" del inventario?") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                dispositivoUseCases.eliminarDispositivo(dispositivo)
                                EventBus.enviar(UiEvent.SUCCESS("Equipo eliminado correctamente."))
                                showDeleteDialog = null
                                reloadTrigger++
                            } catch (e: Exception) {
                                EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudo eliminar el equipo."))
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) { Text("Eliminar") }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = null }) { Text("Cancelar") } }
        )
    }
}

private fun formatFecha(timestamp: Long?): String {
    if (timestamp == null) return "-"
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Composable
fun DeviceItemCard(
    dispositivo: Dispositivo,
    primaryBlue: Color,
    lightBlue: Color,
    onViewHistory: () -> Unit,
    onDelete: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.White, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Laptop, null, tint = primaryBlue, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = dispositivo.marca, fontSize = 11.sp, color = Color.Gray)
                        Text(text = dispositivo.modelo, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar equipo",
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onDelete() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            DeviceField("Nro. Serie:", dispositivo.numeroSerie)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onViewHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(listOf(lightBlue, primaryBlue))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ver Historial", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun AddEquipmentDialog(onDismiss: () -> Unit, onConfirmar: (marca: String, modelo: String, serial: String) -> Unit) {
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var serial by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar Equipo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = marca, onValueChange = { marca = it; errorMsg = "" }, label = { Text("Marca") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = modelo, onValueChange = { modelo = it; errorMsg = "" }, label = { Text("Modelo") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = serial, onValueChange = { serial = it; errorMsg = "" }, label = { Text("Nro. Serie") }, modifier = Modifier.fillMaxWidth())
                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (marca.isNotBlank() && modelo.isNotBlank() && serial.isNotBlank()) {
                        onConfirmar(marca.trim(), modelo.trim(), serial.trim())
                    } else {
                        errorMsg = "Completa marca, modelo y número de serie."
                    }
                }
            ) { Text("Añadir") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun HistoryDialog(dispositivo: Dispositivo, ordenes: List<Orden>, onDismiss: () -> Unit) {
    val totalMonto = ordenes.sumOf { it.totalCobrado }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Historial: ${dispositivo.marca} ${dispositivo.modelo}") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text("Resumen General", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Órdenes registradas: ${ordenes.size}", fontSize = 14.sp)
                Text("Monto total invertido: S/${"%.2f".format(totalMonto)}", fontSize = 14.sp, color = Color(0xFF0D31B1), fontWeight = FontWeight.SemiBold)

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Text("Detalle de intervenciones:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))

                if (ordenes.isEmpty()) {
                    Text("No hay reparaciones registradas aún.", fontSize = 13.sp, color = Color.Gray)
                } else {
                    ordenes.sortedByDescending { it.fechaIngreso }.forEach { orden ->
                        Column(modifier = Modifier.padding(bottom = 12.dp)) {
                            Text("Fecha: ${formatFecha(orden.fechaIngreso)}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("Técnico: ${orden.tecnicoNombre.ifBlank { "Sin asignar" }}", fontSize = 13.sp)
                            Text("Motivo: ${orden.detalleDiagnostico}", fontSize = 13.sp)
                            Text("Estado: ${orden.estado}", fontSize = 13.sp)
                            if (orden.totalCobrado > 0) {
                                Text("Costo: S/${"%.2f".format(orden.totalCobrado)}", fontSize = 13.sp, color = Color.DarkGray)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = { Button(onClick = onDismiss) { Text("Cerrar") } }
    )
}

@Composable
fun DeviceField(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(text = "$label ", fontSize = 13.sp, fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = 13.sp, color = Color.DarkGray)
    }
}
