package com.t2.appaws14753.presentation.screen.admin

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
import com.t2.appaws14753.domain.model.Cliente
import com.t2.appaws14753.domain.model.DetalleServicio
import com.t2.appaws14753.domain.model.Dispositivo
import com.t2.appaws14753.domain.model.Orden
import com.t2.appaws14753.domain.model.Roles
import com.t2.appaws14753.domain.model.Servicio
import com.t2.appaws14753.domain.model.Usuario
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
    val clienteUseCases = remember { AppModule.provideClienteUseCases(context) }
    val usuarioUseCases = remember { AppModule.provideUsuarioUseCases(context) }
    val servicioUseCases = remember { AppModule.provideServicioUseCases(context) }
    val scope = rememberCoroutineScope()

    val primaryBlue = Color(0xFF0D31B1)

    var ordenes by remember { mutableStateOf<List<Orden>>(emptyList()) }
    var dispositivos by remember { mutableStateOf<List<Dispositivo>>(emptyList()) }
    var clientes by remember { mutableStateOf<List<Cliente>>(emptyList()) }
    var tecnicos by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    var servicios by remember { mutableStateOf<List<Servicio>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var reloadTrigger by remember { mutableIntStateOf(0) }

    var showFilterMenu by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Todos los Estados") }

    var showNewOrderDialog by remember { mutableStateOf(false) }
    var showDetailDialog by remember { mutableStateOf<Orden?>(null) }

    LaunchedEffect(reloadTrigger) {
        isLoading = true
        try {
            ordenes = ordenUseCases.getOrdenes()
            dispositivos = dispositivoUseCases.getDispositivos()
            clientes = clienteUseCases.getClientes()
            tecnicos = usuarioUseCases.getUsuarios().filter { Roles.normalizar(it.rol) == Roles.TECNICO }
            servicios = servicioUseCases.getServicios()
        } catch (e: Exception) {
            EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudieron cargar las órdenes."))
        } finally {
            isLoading = false
        }
    }

    fun dispositivoDe(id: String): Dispositivo? = dispositivos.firstOrNull { it.dispositivoId == id }
    fun clienteDe(id: String): Cliente? = clientes.firstOrNull { it.id == id }

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
            text = "Gestiona las reparaciones y mantenimientos",
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
                "Registra al menos un equipo antes de crear una orden.",
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
                    val dispositivo = dispositivoDe(orden.dispositivoId)
                    DetailedOrderCard(
                        orden = orden,
                        equipoNombre = dispositivo?.let { "${it.marca} ${it.modelo}" } ?: "Equipo desconocido",
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
            clientes = clientes,
            tecnicos = tecnicos,
            servicios = servicios,
            onDismiss = { showNewOrderDialog = false },
            onConfirmar = { orden ->
                scope.launch {
                    try {
                        ordenUseCases.insertarOrden(orden)
                        EventBus.enviar(UiEvent.SUCCESS("Orden creada correctamente."))
                        showNewOrderDialog = false
                        reloadTrigger++
                    } catch (e: Exception) {
                        EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudo crear la orden."))
                    }
                }
            }
        )
    }

    showDetailDialog?.let { orden ->
        val dispositivo = dispositivoDe(orden.dispositivoId)
        val cliente = clienteDe(orden.clienteId)
        OrderDetailDialog(
            orden = orden,
            equipoNombre = dispositivo?.let { "${it.marca} ${it.modelo} (${it.numeroSerie})" } ?: "Equipo desconocido",
            clienteNombre = cliente?.nombre ?: "Cliente desconocido",
            onDismiss = { showDetailDialog = null }
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
fun NewOrderDialog(
    dispositivos: List<Dispositivo>,
    clientes: List<Cliente>,
    tecnicos: List<Usuario>,
    servicios: List<Servicio>,
    onDismiss: () -> Unit,
    onConfirmar: (Orden) -> Unit
) {
    var expandedDispositivo by remember { mutableStateOf(false) }
    var selectedDispositivo by remember { mutableStateOf<Dispositivo?>(null) }
    var expandedTecnico by remember { mutableStateOf(false) }
    var selectedTecnico by remember { mutableStateOf<Usuario?>(null) }
    var expandedPrioridad by remember { mutableStateOf(false) }
    var prioridad by remember { mutableStateOf("Baja") }
    var detalle by remember { mutableStateOf("") }
    val serviciosSeleccionados = remember { mutableStateListOf<Servicio>() }
    var errorMsg by remember { mutableStateOf("") }

    fun nombreCliente(clienteId: String): String = clientes.firstOrNull { it.id == clienteId }?.nombre ?: "Cliente desconocido"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Orden") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                ExposedDropdownMenuBox(expanded = expandedDispositivo, onExpandedChange = { expandedDispositivo = it }) {
                    OutlinedTextField(
                        value = selectedDispositivo?.let { "${it.marca} ${it.modelo} - ${nombreCliente(it.clienteId)}" } ?: "Seleccionar equipo...",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Equipo *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDispositivo) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expandedDispositivo, onDismissRequest = { expandedDispositivo = false }) {
                        dispositivos.forEach { dispositivo ->
                            DropdownMenuItem(
                                text = { Text("${dispositivo.marca} ${dispositivo.modelo} - ${nombreCliente(dispositivo.clienteId)}") },
                                onClick = { selectedDispositivo = dispositivo; expandedDispositivo = false; errorMsg = "" }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(expanded = expandedTecnico, onExpandedChange = { expandedTecnico = it }) {
                    OutlinedTextField(
                        value = selectedTecnico?.let { "${it.nombres} ${it.apellidoPaterno}" } ?: "Seleccionar técnico...",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Técnico *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTecnico) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expandedTecnico, onDismissRequest = { expandedTecnico = false }) {
                        if (tecnicos.isEmpty()) {
                            DropdownMenuItem(text = { Text("No hay técnicos registrados") }, onClick = {})
                        }
                        tecnicos.forEach { tecnico ->
                            DropdownMenuItem(
                                text = { Text("${tecnico.nombres} ${tecnico.apellidoPaterno}") },
                                onClick = { selectedTecnico = tecnico; expandedTecnico = false; errorMsg = "" }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(expanded = expandedPrioridad, onExpandedChange = { expandedPrioridad = it }) {
                    OutlinedTextField(
                        value = prioridad,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Prioridad") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPrioridad) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expandedPrioridad, onDismissRequest = { expandedPrioridad = false }) {
                        listOf("Baja", "Media", "Alta").forEach { opcion ->
                            DropdownMenuItem(text = { Text(opcion) }, onClick = { prioridad = opcion; expandedPrioridad = false })
                        }
                    }
                }

                OutlinedTextField(
                    value = detalle,
                    onValueChange = { detalle = it; errorMsg = "" },
                    label = { Text("Diagnóstico / motivo *") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Servicios a aplicar", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                if (servicios.isEmpty()) {
                    Text("No hay servicios registrados.", fontSize = 12.sp, color = Color.Gray)
                } else {
                    servicios.forEach { servicio ->
                        val seleccionado = serviciosSeleccionados.any { it.servicioId == servicio.servicioId }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().clickable {
                                if (seleccionado) serviciosSeleccionados.removeAll { it.servicioId == servicio.servicioId }
                                else serviciosSeleccionados.add(servicio)
                            }
                        ) {
                            Checkbox(checked = seleccionado, onCheckedChange = {
                                if (seleccionado) serviciosSeleccionados.removeAll { it.servicioId == servicio.servicioId }
                                else serviciosSeleccionados.add(servicio)
                            })
                            Text("${servicio.nombreServicio} (S/${"%.2f".format(servicio.precioServicio)})", fontSize = 13.sp)
                        }
                    }
                }

                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val dispositivo = selectedDispositivo
                    val tecnico = selectedTecnico
                    if (dispositivo == null || detalle.isBlank()) {
                        errorMsg = "Selecciona un equipo e ingresa el diagnóstico."
                        return@Button
                    }
                    if (tecnico == null) {
                        errorMsg = "Selecciona un técnico."
                        return@Button
                    }
                    val total = serviciosSeleccionados.sumOf { it.precioServicio }
                    onConfirmar(
                        Orden(
                            dispositivoId = dispositivo.dispositivoId,
                            clienteId = dispositivo.clienteId,
                            tecnicoId = tecnico.usuarioId,
                            tecnicoNombre = "${tecnico.nombres} ${tecnico.apellidoPaterno}",
                            estado = "pendiente",
                            prioridad = prioridad,
                            fechaIngreso = System.currentTimeMillis(),
                            detalleDiagnostico = detalle.trim(),
                            totalCobrado = total,
                            servicios = serviciosSeleccionados.map { DetalleServicio(it.servicioId, it.nombreServicio, it.precioServicio) }
                        )
                    )
                }
            ) { Text("Crear") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun OrderDetailDialog(orden: Orden, equipoNombre: String, clienteNombre: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Detalle de Orden #${orden.ordenId.take(8)}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DetailItem("Cliente:", clienteNombre)
                DetailItem("Equipo:", equipoNombre)
                DetailItem("Técnico:", orden.tecnicoNombre.ifBlank { "Sin asignar" })
                DetailItem("Fecha ingreso:", formatFecha(orden.fechaIngreso))
                DetailItem("Fecha entrega:", formatFecha(orden.fechaEntrega))
                DetailItem("Estado:", orden.estado)
                DetailItem("Prioridad:", orden.prioridad)
                DetailItem("Diagnóstico:", orden.detalleDiagnostico)
                if (orden.servicios.isNotEmpty()) {
                    Text("Servicios:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    orden.servicios.forEach {
                        Text("- ${it.nombreServicio}: S/${"%.2f".format(it.precioServicio)}", fontSize = 12.sp)
                    }
                }
                if (orden.totalCobrado > 0) {
                    DetailItem("Total:", "S/${"%.2f".format(orden.totalCobrado)}")
                }
            }
        },
        confirmButton = { Button(onClick = onDismiss) { Text("Cerrar") } }
    )
}

@Composable
fun DetailItem(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = FontWeight.Bold, modifier = Modifier.width(110.dp))
        Text(text = value)
    }
}
