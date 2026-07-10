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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.t2.appaws14753.domain.model.DataMock
import com.t2.appaws14753.domain.model.OrdenServicio
import com.t2.appaws14753.domain.model.Equipo

@Composable
fun OrderScreen() {
    val primaryBlue = Color(0xFF0D31B1)
    
    var showFilterMenu by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Todos los Estados") }
    
    var showNewOrderDialog by remember { mutableStateOf(false) }
    var showDetailDialog by remember { mutableStateOf<OrdenServicio?>(null) }

    val filteredOrders = remember(selectedFilter, DataMock.ordenes.size) {
        DataMock.ordenes
            .filter { 
                selectedFilter == "Todos los Estados" || it.estado.equals(selectedFilter, ignoreCase = true)
            }
            .sortedByDescending { it.numero }
    }

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
                DropdownMenuItem(
                    text = { Text("Todos los Estados") },
                    onClick = { selectedFilter = "Todos los Estados"; showFilterMenu = false }
                )
                DropdownMenuItem(
                    text = { Text("Completado") },
                    onClick = { selectedFilter = "completado"; showFilterMenu = false }
                )
                DropdownMenuItem(
                    text = { Text("Pendiente") },
                    onClick = { selectedFilter = "pendiente"; showFilterMenu = false }
                )
                DropdownMenuItem(
                    text = { Text("En Proceso") },
                    onClick = { selectedFilter = "en proceso"; showFilterMenu = false }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showNewOrderDialog = true },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
        ) {
            Text("Nueva Orden", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        filteredOrders.forEach { orden ->
            DetailedOrderCard(
                orden = orden,
                onViewDetail = { showDetailDialog = orden }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showNewOrderDialog) {
        NewOrderDialog(onDismiss = { showNewOrderDialog = false })
    }

    showDetailDialog?.let { orden ->
        OrderDetailDialog(orden = orden, onDismiss = { showDetailDialog = null })
    }
}

@Composable
fun DetailedOrderCard(
    orden: OrdenServicio,
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
                    Text(text = "Orden #${orden.numero}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = orden.tipo, fontSize = 12.sp, color = Color.Gray)
                }
                Surface(
                    color = statusColor,
                    shape = RoundedCornerShape(12.dp)
                ) {
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
                Text(text = orden.equipo, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = priorityColor,
                    shape = RoundedCornerShape(8.dp)
                ) {
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
        }
    }
}

@Composable
fun NewOrderDialog(onDismiss: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedEquipo by remember { mutableStateOf<Equipo?>(null) }
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
                        value = selectedEquipo?.nombre ?: "Seleccionar...",
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
                        DataMock.equipos.forEach { equipo ->
                            DropdownMenuItem(
                                text = { Text("${equipo.nombre} (${equipo.nroSerie})") },
                                onClick = {
                                    selectedEquipo = equipo
                                    expanded = false
                                    errorMsg = ""
                                }
                            )
                        }
                    }
                }
                
                if (DataMock.equipos.isEmpty()) {
                    Text(
                        "No tienes equipos registrados. Regístralos primero en la pestaña Equipos.",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val equipo = selectedEquipo
                    if (equipo != null) {
                        DataMock.ordenes.add(
                            OrdenServicio(
                                numero = DataMock.ordenes.size + 1,
                                tipo = "Solicitud nueva",
                                equipo = equipo.nombre,
                                nroSerie = equipo.nroSerie,
                                estado = "pendiente",
                                prioridad = "Baja",
                                actualizado = "Hoy",
                                fechaCreacion = "Hoy"
                            )
                        )
                        onDismiss()
                    } else {
                        errorMsg = "Debes seleccionar un equipo"
                    }
                },
                enabled = selectedEquipo != null
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
fun OrderDetailDialog(orden: OrdenServicio, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Detalle de Orden #${orden.numero}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DetailItem("Técnico:", orden.tecnico)
                DetailItem("Fecha envío:", orden.fechaCreacion)
                DetailItem("ID Orden:", orden.numero.toString())
                DetailItem("Garantía:", orden.fechaGarantia)
                DetailItem("Completado:", orden.fechaCompletado)
                if (orden.costo > 0) {
                    DetailItem("Costo:", "$${orden.costo}")
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
