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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.t2.appaws14753.domain.model.DataMock
import com.t2.appaws14753.domain.model.OrdenServicio

@Composable
fun WalletScreen() {
    val primaryBlue = Color(0xFF0D31B1)
    val lightBlue = Color(0xFF2196F3)

    var showDetailDialog by remember { mutableStateOf<OrdenServicio?>(null) }

    // Lectura directa de DataMock (igual que en HomeScreen): así el total
    // se refresca de inmediato cuando el técnico marca una orden como
    // terminada en OrderScreen, sin depender del tamaño de la lista.
    val ordenesPagadas = DataMock.ordenes
        .filter { it.estado.equals("completado", ignoreCase = true) && it.costo > 0.0 }
        .sortedBy { it.numero }

    val totalGenerado = ordenesPagadas.sumOf { it.costo }
    val fechaInicio = ordenesPagadas.minByOrNull { it.numero }?.fechaCreacion ?: "-"
    val fechaFin = ordenesPagadas
        .map { if (it.fechaCompletado != "-" ) it.fechaCompletado else it.actualizado }
        .maxByOrNull { it } ?: "-"

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
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (ordenesPagadas.isEmpty()) {
            Text(
                text = "Aún no hay órdenes pagadas para mostrar.",
                fontSize = 13.sp,
                color = Color.Gray
            )
        } else {
            ordenesPagadas.forEach { orden ->
                WalletOrderItem(
                    orden = orden,
                    primaryBlue = primaryBlue,
                    onViewDetail = { showDetailDialog = orden }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    showDetailDialog?.let { orden ->
        WalletOrderDetailDialog(orden = orden, primaryBlue = primaryBlue, onDismiss = { showDetailDialog = null })
    }
}

@Composable
fun WalletOrderItem(
    orden: OrdenServicio,
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
                text = "Orden #${orden.numero}:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "S/${"%.2f".format(orden.costo)}",
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
fun WalletOrderDetailDialog(orden: OrdenServicio, primaryBlue: Color, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Orden #${orden.numero}") },
        text = {
            Column {
                DetailRow("Equipo:", orden.equipo)
                DetailRow("Tipo:", orden.tipo)
                DetailRow("Técnico:", orden.tecnico)
                DetailRow("Fecha creación:", orden.fechaCreacion)
                DetailRow("Fecha completado:", orden.fechaCompletado)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Monto cobrado: S/${"%.2f".format(orden.costo)}",
                    fontWeight = FontWeight.Bold,
                    color = primaryBlue,
                    fontSize = 15.sp
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
