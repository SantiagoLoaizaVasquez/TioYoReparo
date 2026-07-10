package com.t2.appaws14753.presentation.screen.admin

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.t2.appaws14753.domain.model.DataMock

@Composable
fun HomeScreen() {
    val primaryBlue = Color(0xFF0D31B1)
    val yellowStatus = Color(0xFFFFEB3B)
    val greenStatus = Color(0xFF4CAF50)
    val orangeStatus = Color(0xFFFF9800)

    // Todo se lee en vivo desde DataMock, la misma fuente que usan
    // las pantallas de Equipos y Ordenes, para que los contadores
    // siempre reflejen lo que realmente hay (agregar/quitar se nota aqui).
    val equiposCount = DataMock.equipos.size
    val ordenesActivas = DataMock.ordenes.count {
        it.estado.equals("pendiente", ignoreCase = true) || it.estado.equals("en proceso", ignoreCase = true)
    }
    val reparacionesFinalizadas = DataMock.ordenes.count {
        it.estado.equals("completado", ignoreCase = true)
    }
    val ordenesRecientes = DataMock.ordenes.sortedByDescending { it.numero }.take(2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Bienvenido, Forastero Perua",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

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
                            id = "${orden.numero}",
                            device = orden.equipo,
                            date = orden.actualizado,
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
