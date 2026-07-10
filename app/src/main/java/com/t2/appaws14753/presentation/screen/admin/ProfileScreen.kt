package com.t2.appaws14753.presentation.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.t2.appaws14753.domain.model.DataMock
import com.t2.appaws14753.presentation.event.EventBus
import com.t2.appaws14753.presentation.event.UiEvent
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen() {
    val primaryBlue = Color(0xFF0D31B1)
    val green = Color(0xFF4CAF50)

    var isEditing by remember { mutableStateOf(false) }

    var draft by remember { mutableStateOf(DataMock.adminProfile.value) }

    val scope = rememberCoroutineScope()

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

        Text(
            text = DataMock.adminProfile.value.nombreCompleto,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "@${DataMock.adminProfile.value.usuario}",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            color = green,
            shape = RoundedCornerShape(50.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(DataMock.adminProfile.value.estado, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                if (isEditing) {
                    OutlinedTextField(
                        value = draft.nombreCompleto,
                        onValueChange = { draft = draft.copy(nombreCompleto = it) },
                        label = { Text("Nombre completo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = draft.usuario,
                        onValueChange = { draft = draft.copy(usuario = it) },
                        label = { Text("Usuario") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = draft.correo,
                        onValueChange = { draft = draft.copy(correo = it) },
                        label = { Text("Correo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = draft.telefono,
                        onValueChange = { draft = draft.copy(telefono = it) },
                        label = { Text("Teléfono") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    ProfileField(icon = Icons.Default.Person, label = "Nombre completo", value = draft.nombreCompleto)
                    ProfileField(icon = Icons.Default.Person, label = "Usuario", value = draft.usuario)
                    ProfileField(icon = Icons.Default.Email, label = "Correo", value = draft.correo)
                    ProfileField(icon = Icons.Default.Phone, label = "Teléfono", value = draft.telefono)
                    ProfileField(icon = Icons.Default.VerifiedUser, label = "Rol", value = draft.rol)
                    ProfileField(icon = Icons.Default.CheckCircle, label = "Estado", value = draft.estado)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isEditing) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        draft = DataMock.adminProfile.value
                        isEditing = false
                    },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = {
                        val nombreValido = draft.nombreCompleto.isNotBlank()
                        val correoValido = draft.correo.isNotBlank() && draft.correo.contains("@")
                        val usuarioValido = draft.usuario.isNotBlank()

                        if (nombreValido && correoValido && usuarioValido) {
                            DataMock.adminProfile.value = draft
                            isEditing = false
                            scope.launch { EventBus.enviar(UiEvent.SUCCESS("Perfil actualizado correctamente.")) }
                        } else {
                            scope.launch { EventBus.enviar(UiEvent.ERROR("Revisa nombre, usuario y correo antes de guardar.")) }
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
                onClick = { draft = DataMock.adminProfile.value; isEditing = true },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
            ) {
                Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Editar Perfil", fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ProfileField(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
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
