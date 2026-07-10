package com.t2.appaws14753.presentation.screen.admin

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.t2.appaws14753.di.AppModule
import com.t2.appaws14753.domain.model.COMISION_TECNICO
import com.t2.appaws14753.domain.model.Cliente
import com.t2.appaws14753.domain.model.Dispositivo
import com.t2.appaws14753.domain.model.Orden
import com.t2.appaws14753.domain.model.Usuario
import com.t2.appaws14753.presentation.event.EventBus
import com.t2.appaws14753.presentation.event.UiEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IngresosScreen() {
    val context = LocalContext.current
    val ordenUseCases = remember { AppModule.provideOrdenUseCases(context) }
    val dispositivoUseCases = remember { AppModule.provideDispositivoUseCases(context) }
    val clienteUseCases = remember { AppModule.provideClienteUseCases(context) }
    val usuarioUseCases = remember { AppModule.provideUsuarioUseCases(context) }

    val primaryBlue = Color(0xFF0D31B1)
    val lightBlue = Color(0xFF2196F3)
    val greenStatus = Color(0xFF4CAF50)
    val orangeStatus = Color(0xFFFF9800)

    var ordenes by remember { mutableStateOf<List<Orden>>(emptyList()) }
    var dispositivos by remember { mutableStateOf<List<Dispositivo>>(emptyList()) }
    var clientes by remember { mutableStateOf<List<Cliente>>(emptyList()) }
    var tecnicos by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var showDetailDialog by remember { mutableStateOf<Orden?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            ordenes = ordenUseCases.getOrdenes()
            dispositivos = dispositivoUseCases.getDispositivos()
            clientes = clienteUseCases.getClientes()
            tecnicos = usuarioUseCases.getUsuarios()
        } catch (e: Exception) {
            EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudieron cargar los ingresos."))
        } finally {
            isLoading = false
        }
    }

    fun equipoDe(dispositivoId: String): String =
        dispositivos.firstOrNull { it.dispositivoId == dispositivoId }?.let { "${it.marca} ${it.modelo}" } ?: "Equipo desconocido"

    fun clienteDe(clienteId: String): String =
        clientes.firstOrNull { it.id == clienteId }?.nombre ?: "Cliente desconocido"

    fun tecnicoDe(tecnicoId: String): String =
        tecnicos.firstOrNull { it.usuarioId == tecnicoId }?.let { "${it.nombres} ${it.apellidoPaterno}" } ?: "Sin asignar"

    val ordenesPagadas = ordenes
        .filter { it.estado.equals("completado", ignoreCase = true) && it.totalCobrado > 0.0 }
        .sortedByDescending { it.fechaIngreso }

    val bruto = ordenesPagadas.sumOf { it.totalCobrado }
    val comisiones = bruto * COMISION_TECNICO
    val neto = bruto - comisiones

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Ingresos del Negocio",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Resumen de todas las órdenes completadas y pagadas",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryBlue)
                }
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(lightBlue, primaryBlue)),
                            RoundedCornerShape(24.dp)
                        )
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "S/${"%.2f".format(bruto)}",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Ingreso bruto total",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IngresoResumenCard(
                        modifier = Modifier.weight(1f),
                        titulo = "Comisiones (${(COMISION_TECNICO * 100).toInt()}%)",
                        subtitulo = "Pagado a técnicos",
                        monto = comisiones,
                        color = orangeStatus
                    )
                    IngresoResumenCard(
                        modifier = Modifier.weight(1f),
                        titulo = "Neto (${((1 - COMISION_TECNICO) * 100).toInt()}%)",
                        subtitulo = "Para el negocio",
                        monto = neto,
                        color = greenStatus
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Detalle por orden",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (ordenesPagadas.isEmpty()) {
                    Text(
                        text = "Aún no hay órdenes pagadas para mostrar.",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                } else {
                    ordenesPagadas.forEach { orden ->
                        IngresoOrdenItem(
                            orden = orden,
                            clienteNombre = clienteDe(orden.clienteId),
                            primaryBlue = primaryBlue,
                            onViewDetail = { showDetailDialog = orden }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    showDetailDialog?.let { orden ->
        IngresoDetailDialog(
            orden = orden,
            equipoNombre = equipoDe(orden.dispositivoId),
            clienteNombre = clienteDe(orden.clienteId),
            tecnicoNombre = tecnicoDe(orden.tecnicoId),
            primaryBlue = primaryBlue,
            onDismiss = { showDetailDialog = null }
        )
    }
}

@Composable
fun IngresoResumenCard(
    modifier: Modifier = Modifier,
    titulo: String,
    subtitulo: String,
    monto: Double,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "S/${"%.2f".format(monto)}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = color)
            Text(text = titulo, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 4.dp))
            Text(text = subtitulo, fontSize = 11.sp, color = Color.Gray)
        }
    }
}

@Composable
fun IngresoOrdenItem(
    orden: Orden,
    clienteNombre: String,
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
            Column {
                Text(text = "Orden #${orden.ordenId.take(8)}", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text(text = clienteNombre, fontSize = 11.sp, color = Color.Gray)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "S/${"%.2f".format(orden.totalCobrado)}",
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
fun IngresoDetailDialog(
    orden: Orden,
    equipoNombre: String,
    clienteNombre: String,
    tecnicoNombre: String,
    primaryBlue: Color,
    onDismiss: () -> Unit
) {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val comisionTecnico = orden.totalCobrado * COMISION_TECNICO
    val netoNegocio = orden.totalCobrado - comisionTecnico

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Orden #${orden.ordenId.take(8)}") },
        text = {
            Column {
                DetailRowIngreso("Cliente:", clienteNombre)
                DetailRowIngreso("Equipo:", equipoNombre)
                DetailRowIngreso("Técnico:", tecnicoNombre)
                DetailRowIngreso("Fecha ingreso:", sdf.format(Date(orden.fechaIngreso)))
                DetailRowIngreso("Fecha completado:", orden.fechaEntrega?.let { sdf.format(Date(it)) } ?: "-")

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Monto bruto: S/${"%.2f".format(orden.totalCobrado)}",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "Comisión técnico (${(COMISION_TECNICO * 100).toInt()}%): S/${"%.2f".format(comisionTecnico)}",
                    fontSize = 13.sp,
                    color = Color(0xFFFF9800)
                )
                Text(
                    text = "Neto para el negocio: S/${"%.2f".format(netoNegocio)}",
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
fun DetailRowIngreso(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(text = "$label ", fontSize = 13.sp, fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = 13.sp, color = Color.DarkGray)
    }
}
