package com.t2.appaws14753.domain.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class UsuarioSesion(
    val email: String,
    val tipo: String,
    val nombre: String,
    val usuarioId: String
)

object SesionManager {
    var actual by mutableStateOf<UsuarioSesion?>(null)

    fun iniciarSesion(usuario: UsuarioSesion) {
        actual = usuario
    }

    fun cerrarSesion() {
        actual = null
    }
}

object Roles {
    const val ADMIN = "admin"
    const val CLIENTE = "cliente"
    const val TECNICO = "tecnico"

    fun normalizar(rol: String?): String = rol?.trim()?.lowercase().orEmpty()
}

object DataMock {
    // Cuentas de prueba: se conservan únicamente para el login (ver LoginScreen).
    // El resto de la app ya no usa datos moqueados; todas las pantallas trabajan
    // con datos reales provenientes de Room a través de AppModule.
    val usuarios = listOf(
        UsuarioSesion("admin@hardware.com", Roles.ADMIN, "Administrador", "mock-admin"),
        UsuarioSesion("cliente@hardware.com", Roles.CLIENTE, "Forastero Perua", "mock-cliente"),
        UsuarioSesion("tecnico@hardware.com", Roles.TECNICO, "Técnico Principal", "mock-tecnico")
    )

    fun autenticar(email: String, password: String): UsuarioSesion? {
        return when {
            email == "admin@hardware.com" && password == "admin123" -> usuarios[0]
            email == "cliente@hardware.com" && password == "cliente123" -> usuarios[1]
            email == "tecnico@hardware.com" && password == "tecnico123" -> usuarios[2]
            else -> null
        }
    }
}


