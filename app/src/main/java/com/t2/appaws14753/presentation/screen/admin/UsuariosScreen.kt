package com.t2.appaws14753.presentation.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.t2.appaws14753.di.AppModule
import com.t2.appaws14753.domain.model.Cliente
import com.t2.appaws14753.domain.model.Roles
import com.t2.appaws14753.domain.model.Usuario
import com.t2.appaws14753.presentation.event.EventBus
import com.t2.appaws14753.presentation.event.UiEvent
import kotlinx.coroutines.launch

private val rolesDisponibles = listOf(
    "Administrador" to Roles.ADMIN,
    "Técnico" to Roles.TECNICO,
    "Cliente" to Roles.CLIENTE
)

private fun labelDeRol(rol: String): String =
    rolesDisponibles.firstOrNull { it.second == Roles.normalizar(rol) }?.first ?: rol

@Composable
fun UsuariosScreen() {
    val context = LocalContext.current
    val usuarioUseCases = remember { AppModule.provideUsuarioUseCases(context) }
    val clienteUseCases = remember { AppModule.provideClienteUseCases(context) }
    val scope = rememberCoroutineScope()

    val primaryBlue = Color(0xFF0D31B1)

    var usuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var showFormDialog by remember { mutableStateOf(false) }
    var usuarioEnEdicion by remember { mutableStateOf<Usuario?>(null) }
    var showDeleteDialog by remember { mutableStateOf<Usuario?>(null) }
    var reloadTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(reloadTrigger) {
        isLoading = true
        try {
            usuarios = usuarioUseCases.getUsuarios()
        } catch (e: Exception) {
            EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudieron cargar los usuarios."))
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = null,
                tint = primaryBlue,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Gestión de Usuarios",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Administra las cuentas del sistema",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                usuarioEnEdicion = null
                showFormDialog = true
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
        ) {
            Icon(Icons.Default.Add, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Añadir Usuario", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryBlue)
                }
            }
            usuarios.isEmpty() -> {
                Text(
                    text = "Aún no hay usuarios registrados.",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
            else -> {
                Text(
                    text = "${usuarios.size} usuario(s) registrado(s)",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                usuarios.forEach { usuario ->
                    UsuarioItemCard(
                        usuario = usuario,
                        primaryBlue = primaryBlue,
                        onEdit = {
                            usuarioEnEdicion = usuario
                            showFormDialog = true
                        },
                        onDelete = { showDeleteDialog = usuario }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showFormDialog) {
        val esEdicion = usuarioEnEdicion != null
        UsuarioFormDialog(
            usuarioExistente = usuarioEnEdicion,
            onDismiss = { showFormDialog = false },
            onGuardar = { usuario ->
                scope.launch {
                    try {
                        if (esEdicion) {
                            usuarioUseCases.actualizarUsuario(usuario)
                            EventBus.enviar(UiEvent.SUCCESS("Usuario \"${usuario.nombres}\" actualizado correctamente."))
                        } else {
                            usuarioUseCases.insertarUsuario(usuario)
                            EventBus.enviar(UiEvent.SUCCESS("Usuario \"${usuario.nombres}\" agregado correctamente."))
                        }

                        // Si el usuario tiene rol Cliente, mantenemos sincronizado un
                        // registro Cliente con el mismo id para que Ordenes/Dispositivos
                        // puedan referenciarlo correctamente (clienteId == usuarioId).
                        if (Roles.normalizar(usuario.rol) == Roles.CLIENTE) {
                            clienteUseCases.insertarCliente(
                                Cliente(
                                    id = usuario.usuarioId,
                                    nombre = "${usuario.nombres} ${usuario.apellidoPaterno}".trim(),
                                    email = usuario.correo,
                                    telefono = ""
                                )
                            )
                        }

                        showFormDialog = false
                        reloadTrigger++
                    } catch (e: Exception) {
                        EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudo guardar el usuario."))
                    }
                }
            }
        )
    }

    showDeleteDialog?.let { usuario ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Eliminar usuario") },
            text = { Text("¿Seguro que deseas eliminar a \"${usuario.nombres} ${usuario.apellidoPaterno}\"?") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                usuarioUseCases.eliminarUsuario(usuario)

                                if (Roles.normalizar(usuario.rol) == Roles.CLIENTE) {
                                    clienteUseCases.getClienteById(usuario.usuarioId)?.let { cliente ->
                                        clienteUseCases.eliminarCliente(cliente)
                                    }
                                }

                                EventBus.enviar(UiEvent.SUCCESS("Usuario eliminado correctamente."))
                                showDeleteDialog = null
                                reloadTrigger++
                            } catch (e: Exception) {
                                EventBus.enviar(UiEvent.ERROR(e.message ?: "No se pudo eliminar el usuario."))
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

@Composable
fun UsuarioItemCard(
    usuario: Usuario,
    primaryBlue: Color,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (Roles.normalizar(usuario.rol) == Roles.ADMIN) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                contentDescription = null,
                tint = primaryBlue,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${usuario.nombres} ${usuario.apellidoPaterno}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(text = usuario.correo, fontSize = 12.sp, color = Color.Gray)
                Surface(
                    color = primaryBlue,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = labelDeRol(usuario.rol),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar usuario",
                tint = primaryBlue,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onEdit() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar usuario",
                tint = Color(0xFFD32F2F),
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onDelete() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioFormDialog(
    usuarioExistente: Usuario? = null,
    onDismiss: () -> Unit,
    onGuardar: (Usuario) -> Unit
) {
    val esEdicion = usuarioExistente != null

    var nombres by remember { mutableStateOf(usuarioExistente?.nombres ?: "") }
    var apellidoPaterno by remember { mutableStateOf(usuarioExistente?.apellidoPaterno ?: "") }
    var apellidoMaterno by remember { mutableStateOf(usuarioExistente?.apellidoMaterno ?: "") }
    var correo by remember { mutableStateOf(usuarioExistente?.correo ?: "") }
    var contrasena by remember { mutableStateOf(usuarioExistente?.contrasena ?: "") }
    var contrasenaVisible by remember { mutableStateOf(false) }
    var especialidad by remember { mutableStateOf(usuarioExistente?.especialidad ?: "") }
    var rolSeleccionado by remember {
        mutableStateOf(
            usuarioExistente?.let { u -> rolesDisponibles.firstOrNull { it.second == Roles.normalizar(u.rol) } }
                ?: rolesDisponibles.first()
        )
    }
    var expandedRol by remember { mutableStateOf(false) }
    var errorLocal by remember { mutableStateOf<String?>(null) }

    val primaryBlue = Color(0xFF0D31B1)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (esEdicion) "Editar Usuario" else "Añadir Usuario") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = nombres,
                    onValueChange = { nombres = it; errorLocal = null },
                    label = { Text("Nombres *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = apellidoPaterno,
                    onValueChange = { apellidoPaterno = it; errorLocal = null },
                    label = { Text("Apellido paterno *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = apellidoMaterno,
                    onValueChange = { apellidoMaterno = it },
                    label = { Text("Apellido materno") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it; errorLocal = null },
                    label = { Text("Correo *") },
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it; errorLocal = null },
                    label = { Text("Contraseña *") },
                    singleLine = true,
                    visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                            Icon(
                                imageVector = if (contrasenaVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expandedRol,
                    onExpandedChange = { expandedRol = it }
                ) {
                    OutlinedTextField(
                        value = rolSeleccionado.first,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Rol *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRol) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedRol,
                        onDismissRequest = { expandedRol = false }
                    ) {
                        rolesDisponibles.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion.first) },
                                onClick = {
                                    rolSeleccionado = opcion
                                    expandedRol = false
                                }
                            )
                        }
                    }
                }

                if (rolSeleccionado.second == Roles.TECNICO) {
                    OutlinedTextField(
                        value = especialidad,
                        onValueChange = { especialidad = it },
                        label = { Text("Especialidad") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

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
                    val nombresValido = nombres.isNotBlank()
                    val apellidoValido = apellidoPaterno.isNotBlank()
                    val correoValido = correo.isNotBlank() && correo.contains("@")
                    val contrasenaValida = contrasena.isNotBlank()

                    if (!nombresValido || !apellidoValido || !correoValido || !contrasenaValida) {
                        errorLocal = "Completa nombres, apellido paterno, correo válido y contraseña."
                        return@Button
                    }

                    val datosActualizados = Usuario(
                        usuarioId = usuarioExistente?.usuarioId ?: java.util.UUID.randomUUID().toString(),
                        rol = rolSeleccionado.second,
                        correo = correo.trim(),
                        contrasena = contrasena,
                        nombres = nombres.trim(),
                        apellidoPaterno = apellidoPaterno.trim(),
                        apellidoMaterno = apellidoMaterno.trim().ifBlank { null },
                        especialidad = especialidad.trim().ifBlank { null }
                    )
                    onGuardar(datosActualizados)
                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
            ) { Text(if (esEdicion) "Guardar" else "Añadir") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
