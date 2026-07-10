package com.t2.appaws14753.presentation.screen.admin

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
import com.t2.appaws14753.domain.model.Cliente
import com.t2.appaws14753.domain.model.Dispositivo
import com.t2.appaws14753.domain.model.Orden
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
    val clienteUseCases = remember { AppModule.provideClienteUseCases(context) }
    val ordenUseCases = remember { AppModule.provideOrdenUseCases(context) }
    val scope = rememberCoroutineScope()

    val primaryBlue = Color(0xFF0D31B1)
    val lightBlue = Color(0xFF2196F3)

    var dispositivos by remember { mutableStateOf<List<Dispositivo>>(emptyList()) }
    var clientes by remember { mutableStateOf<List<Cliente>>(emptyList()) }
    var ordenes by remember { mutableStateOf<List<Orden>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var reloadTrigger by remember { mutableIntStateOf(0) }

    var showAddDialog by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf<Dispositivo?>(null) }
    var showDeleteDialog by remember { mutableStateOf<Dispositivo?>(null) }

    LaunchedEffect(reloadTrigger) {
        isLoading = true
        try {
            dispositivos = dispositivoUseCases.getDispositivos()
            clientes = clienteUseCases.getClientes()
            ordenes = ordenUseCases.getOrdenes()
        } catch (e: Exception) {
            EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudieron cargar los equipos."))
        } finally {
            isLoading = false
        }
    }

    fun nombreCliente(clienteId: String): String =
        clientes.firstOrNull { it.id == clienteId }?.nombre ?: "Cliente desconocido"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Inventario de Equipos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Equipos registrados de todos los clientes",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
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
                Text(
                    text = "No hay equipos registrados aún.",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
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
                        propietario = nombreCliente(dispositivo.clienteId),
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
            clientes = clientes,
            onDismiss = { showAddDialog = false },
            onConfirmar = { dispositivo ->
                scope.launch {
                    try {
                        dispositivoUseCases.insertarDispositivo(dispositivo)
                        EventBus.enviar(UiEvent.SUCCESS("Equipo \"${dispositivo.marca} ${dispositivo.modelo}\" registrado correctamente."))
                        showAddDialog = false
                        reloadTrigger++
                    } catch (e: Exception) {
                        EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudo registrar el equipo."))
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
    propietario: String,
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

            DeviceField("Propietario:", propietario)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEquipmentDialog(
    clientes: List<Cliente>,
    onDismiss: () -> Unit,
    onConfirmar: (Dispositivo) -> Unit
) {
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var serial by remember { mutableStateOf("") }
    var expandedCliente by remember { mutableStateOf(false) }
    var clienteSeleccionado by remember { mutableStateOf<Cliente?>(null) }
    var errorMsg by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar Equipo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expandedCliente,
                    onExpandedChange = { expandedCliente = it }
                ) {
                    OutlinedTextField(
                        value = clienteSeleccionado?.nombre ?: "Seleccionar cliente...",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Cliente propietario *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCliente) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCliente,
                        onDismissRequest = { expandedCliente = false }
                    ) {
                        if (clientes.isEmpty()) {
                            DropdownMenuItem(text = { Text("No hay clientes registrados") }, onClick = {})
                        }
                        clientes.forEach { cliente ->
                            DropdownMenuItem(
                                text = { Text("${cliente.nombre} (${cliente.email})") },
                                onClick = {
                                    clienteSeleccionado = cliente
                                    expandedCliente = false
                                    errorMsg = ""
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(value = marca, onValueChange = { marca = it; errorMsg = "" }, label = { Text("Marca *") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = modelo, onValueChange = { modelo = it; errorMsg = "" }, label = { Text("Modelo *") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = serial, onValueChange = { serial = it; errorMsg = "" }, label = { Text("Nro. Serie *") }, singleLine = true, modifier = Modifier.fillMaxWidth())

                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cliente = clienteSeleccionado
                    if (cliente == null || marca.isBlank() || modelo.isBlank() || serial.isBlank()) {
                        errorMsg = "Completa cliente, marca, modelo y número de serie."
                        return@Button
                    }
                    onConfirmar(
                        Dispositivo(
                            clienteId = cliente.id,
                            marca = marca.trim(),
                            modelo = modelo.trim(),
                            numeroSerie = serial.trim()
                        )
                    )
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

                Text("Detalle de órdenes:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))

                if (ordenes.isEmpty()) {
                    Text("No hay órdenes registradas aún.", fontSize = 13.sp, color = Color.Gray)
                } else {
                    ordenes.sortedByDescending { it.fechaIngreso }.forEach { orden ->
                        Column(modifier = Modifier.padding(bottom = 12.dp)) {
                            Text("Ingreso: ${formatFecha(orden.fechaIngreso)}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("Técnico: ${orden.tecnicoNombre.ifBlank { "Sin asignar" }}", fontSize = 13.sp)
                            Text("Estado: ${orden.estado}", fontSize = 13.sp)
                            Text("Diagnóstico: ${orden.detalleDiagnostico}", fontSize = 13.sp)
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
