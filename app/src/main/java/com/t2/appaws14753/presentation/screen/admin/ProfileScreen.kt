package com.t2.appaws14753.presentation.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.t2.appaws14753.di.AppModule
import com.t2.appaws14753.domain.model.SesionManager
import com.t2.appaws14753.domain.model.Usuario
import com.t2.appaws14753.presentation.event.EventBus
import com.t2.appaws14753.presentation.event.UiEvent
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(onLogout: () -> Unit = {}) {
    val context = LocalContext.current
    val usuarioUseCases = remember { AppModule.provideUsuarioUseCases(context) }
    val scope = rememberCoroutineScope()

    val primaryBlue = Color(0xFF0D31B1)
    val green = Color(0xFF4CAF50)

    val sesion = SesionManager.actual

    var usuario by remember { mutableStateOf<Usuario?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isEditing by remember { mutableStateOf(false) }
    var draft by remember { mutableStateOf<Usuario?>(null) }

    LaunchedEffect(sesion?.usuarioId) {
        isLoading = true
        try {
            usuario = sesion?.usuarioId?.let { usuarioUseCases.getUsuarioById(it) }
        } catch (e: NoSuchElementException) {
            usuario = null
        } catch (e: Exception) {
            EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudo cargar el perfil."))
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(96.dp)
                .background(primaryBlue.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Foto de perfil",
                tint = primaryBlue,
                modifier = Modifier.size(56.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val nombreMostrado = usuario?.let { "${it.nombres} ${it.apellidoPaterno}" } ?: (sesion?.nombre ?: "Administrador")
        val correoMostrado = usuario?.correo ?: (sesion?.email ?: "")

        Text(text = nombreMostrado, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(text = correoMostrado, fontSize = 13.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(8.dp))

        Surface(color = green, shape = RoundedCornerShape(50.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Activo", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryBlue)
                }
            }
            usuario == null -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Esta cuenta de prueba no tiene un registro de usuario real, por lo que el perfil no se puede editar.",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
            else -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        val actual = draft ?: usuario!!
                        if (isEditing) {
                            OutlinedTextField(
                                value = actual.nombres,
                                onValueChange = { draft = actual.copy(nombres = it) },
                                label = { Text("Nombres") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = actual.apellidoPaterno,
                                onValueChange = { draft = actual.copy(apellidoPaterno = it) },
                                label = { Text("Apellido paterno") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = actual.correo,
                                onValueChange = { draft = actual.copy(correo = it) },
                                label = { Text("Correo") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            ProfileField(icon = Icons.Default.Person, label = "Nombres", value = actual.nombres)
                            ProfileField(icon = Icons.Default.Person, label = "Apellido paterno", value = actual.apellidoPaterno)
                            ProfileField(icon = Icons.Default.Email, label = "Correo", value = actual.correo)
                            ProfileField(icon = Icons.Default.VerifiedUser, label = "Rol", value = "Administrador")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (isEditing) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(
                            onClick = { draft = usuario; isEditing = false },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(50.dp)
                        ) { Text("Cancelar") }
                        Button(
                            onClick = {
                                val datos = draft ?: return@Button
                                val nombreValido = datos.nombres.isNotBlank()
                                val apellidoValido = datos.apellidoPaterno.isNotBlank()
                                val correoValido = datos.correo.isNotBlank() && datos.correo.contains("@")

                                if (nombreValido && apellidoValido && correoValido) {
                                    scope.launch {
                                        try {
                                            usuarioUseCases.actualizarUsuario(datos)
                                            usuario = datos
                                            isEditing = false
                                            EventBus.enviar(UiEvent.SUCCESS("Perfil actualizado correctamente."))
                                        } catch (e: Exception) {
                                            EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudo actualizar el perfil."))
                                        }
                                    }
                                } else {
                                    scope.launch { EventBus.enviar(UiEvent.ERROR("Revisa nombres, apellido y correo antes de guardar.")) }
                                }
                            },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                        ) {
                            Icon(Icons.Default.Save, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Guardar")
                        }
                    }
                } else {
                    Button(
                        onClick = { draft = usuario; isEditing = true },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                    ) {
                        Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Editar Perfil", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar Sesión", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ProfileField(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Color(0xFF0D31B1), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, fontSize = 11.sp, color = Color.Gray)
            Text(text = value.ifBlank { "—" }, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        }
    }
}
