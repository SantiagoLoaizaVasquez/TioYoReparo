package com.t2.appaws14753.presentation.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.t2.appaws14753.domain.model.DataMock
import com.t2.appaws14753.domain.model.Tecnico
import com.t2.appaws14753.presentation.event.EventBus
import com.t2.appaws14753.presentation.event.UiEvent
import kotlinx.coroutines.launch

@Composable
fun TecnicosScreen() {

    val primaryBlue = Color(0xFF0D31B1)
    val yellow = Color(0xFFFFEB3B)
    val green = Color(0xFF4CAF50)

    var showAddDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Default.Build,
            contentDescription = null,
            tint = primaryBlue,
            modifier = Modifier.size(45.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Gestión Técnicos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Administra personal del mantenimiento",
            color = Color.Gray,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
        ) {
            Icon(Icons.Default.Add, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Añadir Técnico", fontWeight = FontWeight.SemiBold)
        }



        Spacer(modifier = Modifier.height(30.dp))

        if (DataMock.tecnicos.isEmpty()) {
            Text(
                text = "Aún no hay técnicos registrados.",
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            DataMock.tecnicos.forEach { tecnico ->
                TecnicoItemCard(
                    tecnico = tecnico,
                    primaryBlue = primaryBlue,
                    yellow = yellow,
                    green = green
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showAddDialog) {
        AddTecnicoDialog(
            onDismiss = { showAddDialog = false },
            onResult = { success, message ->
                scope.launch {
                    if (success) {
                        EventBus.enviar(UiEvent.SUCCESS(message))
                    } else {
                        EventBus.enviar(UiEvent.ERROR(message))
                    }
                }
            }
        )
    }
}

@Composable
fun TecnicoItemCard(
    tecnico: Tecnico,
    primaryBlue: Color,
    yellow: Color,
    green: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(70.dp),
                tint = primaryBlue
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = tecnico.nombre.uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Text(
                text = tecnico.especialidad,
                color = Color.Gray
            )

            Text(
                text = tecnico.email,
                color = Color.Gray,
                fontSize = 12.sp
            )

            Text(
                text = tecnico.telefono,
                color = Color.Gray,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Icon(
                        Icons.Default.Refresh,
                        null,
                        tint = yellow
                    )

                    Text(
                        "${tecnico.activas}",
                        fontWeight = FontWeight.Bold
                    )

                    Text("Activas")
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Icon(
                        Icons.Default.CheckCircle,
                        null,
                        tint = green
                    )

                    Text(
                        "${tecnico.completadas}",
                        fontWeight = FontWeight.Bold
                    )

                    Text("Completadas")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "⭐ Calificación: ${tecnico.calificacion} / 5.0",
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Última actividad: ${tecnico.ultimaActividad}",
                color = Color.Gray
            )
        }
    }
}

@Composable
fun AddTecnicoDialog(
    onDismiss: () -> Unit,
    onResult: (success: Boolean, message: String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var errorLocal by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir Técnico") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it; errorLocal = null },
                    label = { Text("Nombre completo *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it; errorLocal = null },
                    label = { Text("Correo *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it; errorLocal = null },
                    label = { Text("Teléfono *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = especialidad,
                    onValueChange = { especialidad = it },
                    label = { Text("Especialidad") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorLocal != null) {
                    Text(
                        text = errorLocal.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val nombreValido = nombre.isNotBlank()
                    val correoValido = correo.isNotBlank() && correo.contains("@")
                    val telefonoValido = telefono.isNotBlank()

                    if (!nombreValido || !correoValido || !telefonoValido) {
                        errorLocal = "Completa nombre, correo válido y teléfono."
                        return@Button
                    }

                    val correoDuplicado = DataMock.tecnicos.any {
                        it.email.equals(correo.trim(), ignoreCase = true)
                    }
                    if (correoDuplicado) {
                        errorLocal = "Ya existe un técnico con ese correo."
                        return@Button
                    }

                    DataMock.tecnicos.add(
                        Tecnico(
                            id = (DataMock.tecnicos.maxOfOrNull { it.id } ?: 0) + 1,
                            nombre = nombre.trim(),
                            email = correo.trim(),
                            telefono = telefono.trim(),
                            especialidad = especialidad.trim().ifBlank { "General" },
                            activas = 0,
                            completadas = 0,
                            calificacion = 0.0,
                            ultimaActividad = "Recién agregado"
                        )
                    )
                    onResult(true, "Técnico \"${nombre.trim()}\" agregado correctamente.")
                    onDismiss()
                }
            ) { Text("Añadir") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
