package com.t2.appaws14753.presentation.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.t2.appaws14753.di.AppModule
import com.t2.appaws14753.domain.model.DataMock
import com.t2.appaws14753.domain.model.UsuarioSesion
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginExitoso: (UsuarioSesion) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(value = false) }
    var error by remember { mutableStateOf("") }
    var validando by remember { mutableStateOf(false) }

    val primaryBlue = Color(0xFF0D31B1)
    val logoBlue = Color(0xFF7B92FF)
    val lightGrey = Color(0xFFE0E0E0)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(logoBlue, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Tio YoReparo",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Sistema de mantenimiento y reparación",
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Correo Electrónico",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; error = "" },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Email, null, tint = primaryBlue, modifier = Modifier.size(20.dp)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                shape = RoundedCornerShape(50.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = lightGrey,
                    focusedBorderColor = primaryBlue
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Contraseña",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; error = "" },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Lock, null, tint = primaryBlue, modifier = Modifier.size(20.dp)) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                shape = RoundedCornerShape(50.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = lightGrey,
                    focusedBorderColor = primaryBlue
                )
            )

            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val usuarioMock = DataMock.autenticar(email.trim(), password.trim())
                    if (usuarioMock != null) {
                        onLoginExitoso(usuarioMock)
                        return@Button
                    }

                    validando = true
                    scope.launch {
                        try {
                            val correoIngresado = email.trim()
                            val passwordIngresado = password.trim()
                            val usuarioReal = AppModule.provideUsuarioUseCases(context)
                                .getUsuarios()
                                .firstOrNull {
                                    it.correo.equals(correoIngresado, ignoreCase = true) &&
                                        it.contrasena == passwordIngresado
                                }

                            if (usuarioReal != null) {
                                onLoginExitoso(
                                    UsuarioSesion(
                                        email = usuarioReal.correo,
                                        tipo = usuarioReal.rol,
                                        nombre = usuarioReal.nombres,
                                        usuarioId = usuarioReal.usuarioId
                                    )
                                )
                            } else {
                                error = "Correo o contraseña incorrectos"
                            }
                        } catch (e: Exception) {
                            error = "Correo o contraseña incorrectos"
                        } finally {
                            validando = false
                        }
                    }
                },
                modifier = Modifier
                    .width(180.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                enabled = email.isNotBlank() && password.isNotBlank() && !validando
            ) {
                if (validando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("INICIAR SESION", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Usuarios de Prueba",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(lightGrey, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    InfoUsuarioRow("Admin", "admin@hardware.com") {
                        email = "admin@hardware.com"
                        password = "admin123"
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    InfoUsuarioRow("Cliente", "cliente@hardware.com") {
                        email = "cliente@hardware.com"
                        password = "cliente123"
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    InfoUsuarioRow("Técnico", "tecnico@hardware.com") {
                        email = "tecnico@hardware.com"
                        password = "tecnico123"
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Contraseñas",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Usuario(Tipo)123", fontSize = 11.sp)
                Text("Ejemplo: admin123", fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun InfoUsuarioRow(tipo: String, correo: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = "$tipo : ",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        Text(
            text = correo,
            fontSize = 12.sp,
            color = Color(0xFF0D31B1)
        )
    }
}
