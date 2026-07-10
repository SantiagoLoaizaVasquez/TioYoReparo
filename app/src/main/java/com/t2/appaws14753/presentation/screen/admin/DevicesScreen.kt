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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.t2.appaws14753.domain.model.DataMock
import com.t2.appaws14753.domain.model.Equipo

@Composable
fun DevicesScreen() {
    val primaryBlue = Color(0xFF0D31B1)
    val lightBlue = Color(0xFF2196F3)
    val greenStatus = Color(0xFF4CAF50)
    val yellowStatus = Color(0xFFFFEB3B)

    var showAddDialog by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf<Equipo?>(null) }
    var showDeleteDialog by remember { mutableStateOf<Equipo?>(null) }

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
            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
        ) {
            Icon(Icons.Default.Add, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Registrar Equipo", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "${DataMock.equipos.size} equipo(s) registrado(s)",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (DataMock.equipos.isEmpty()) {
            Text(
                text = "No hay equipos registrados aún.",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        DataMock.equipos.forEach { equipo ->
            val statusColor = if (equipo.estado.lowercase() == "activo") greenStatus else yellowStatus
            DeviceItemCard(
                equipo = equipo,
                statusColor = statusColor,
                primaryBlue = primaryBlue,
                lightBlue = lightBlue,
                onViewHistory = { showHistoryDialog = equipo },
                onDelete = { showDeleteDialog = equipo }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showAddDialog) {
        AddEquipmentDialog(onDismiss = { showAddDialog = false })
    }

    showHistoryDialog?.let { equipo ->
        HistoryDialog(equipo = equipo, onDismiss = { showHistoryDialog = null })
    }

    showDeleteDialog?.let { equipo ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Eliminar equipo") },
            text = { Text("¿Seguro que deseas eliminar \"${equipo.nombre}\" del inventario? Esta acción se reflejará en todas las pantallas.") },
            confirmButton = {
                Button(
                    onClick = {
                        DataMock.equipos.remove(equipo)
                        showDeleteDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) { Text("Eliminar") }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = null }) { Text("Cancelar") } }
        )
    }
}

@Composable
fun DeviceItemCard(
    equipo: Equipo,
    statusColor: Color,
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
                        Text(text = equipo.marca, fontSize = 11.sp, color = Color.Gray)
                        Text(text = equipo.nombre, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = statusColor,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = equipo.estado,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar equipo",
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onDelete() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            DeviceField("Nro. Serie:", equipo.nroSerie)
            DeviceField("Garantía Hasta:", equipo.garantiaHasta)
            DeviceField("Último manto:", equipo.ultimoManto)

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
fun AddEquipmentDialog(onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var serial by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar Equipo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Dispositivo") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Marca") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = serial, onValueChange = { serial = it }, label = { Text("Nro. Serie") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && brand.isNotBlank() && serial.isNotBlank()) {
                        DataMock.equipos.add(
                            Equipo(
                                id = DataMock.equipos.size + 1,
                                nombre = name,
                                marca = brand,
                                nroSerie = serial,
                                garantiaHasta = "Por definir",
                                ultimoManto = "Hoy",
                                estado = "Activo"
                            )
                        )
                        onDismiss()
                    }
                }
            ) { Text("Añadir") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun HistoryDialog(equipo: Equipo, onDismiss: () -> Unit) {
    val totalMonto = equipo.historial.sumOf { it.monto }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Historial: ${equipo.nombre}") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text("Resumen General", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Veces reparado: ${equipo.historial.size}", fontSize = 14.sp)
                Text("Monto total invertido: $${totalMonto}", fontSize = 14.sp, color = Color(0xFF0D31B1), fontWeight = FontWeight.SemiBold)
                Text("Garantía actual: ${equipo.garantiaHasta}", fontSize = 14.sp)
                
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Detalle de intervenciones:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                
                if (equipo.historial.isEmpty()) {
                    Text("No hay reparaciones registradas aún.", fontSize = 13.sp, color = Color.Gray)
                } else {
                    equipo.historial.forEach { rep ->
                        Column(modifier = Modifier.padding(bottom = 12.dp)) {
                            Text("Fecha: ${rep.fecha}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("Técnico: ${rep.tecnico}", fontSize = 13.sp)
                            Text("Motivo: ${rep.motivo}", fontSize = 13.sp)
                            Text("Costo: $${rep.monto}", fontSize = 13.sp, color = Color.DarkGray)
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
