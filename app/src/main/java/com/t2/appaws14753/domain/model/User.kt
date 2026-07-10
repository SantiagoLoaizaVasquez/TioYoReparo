package com.t2.appaws14753.domain.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

data class UsuarioSesion(
    val email: String,
    val tipo: String,
    val nombre: String
)

/**
 * Roles válidos dentro de la aplicación.
 * Centralizar estas constantes evita errores de tipeo/mayúsculas al comparar
 * el rol de sesión contra el destino de navegación (bug típico de que el
 * login "no navega" cuando el string no calza exactamente).
 */
object Roles {
    const val ADMIN = "admin"
    const val CLIENTE = "cliente"
    const val TECNICO = "tecnico"

    /** Normaliza un rol recibido (espacios y mayúsculas) para compararlo de forma segura. */
    fun normalizar(rol: String?): String = rol?.trim()?.lowercase().orEmpty()
}

/** Perfil editable del administrador, mostrado/editado en AdminProfileScreen. */
data class AdminProfile(
    val nombreCompleto: String,
    val usuario: String,
    val correo: String,
    val telefono: String,
    val rol: String,
    val estado: String,
    val fotoUrl: String? = null
)

object DataMock {
    val usuarios = listOf(
        UsuarioSesion("admin@hardware.com", Roles.ADMIN, "Administrador"),
        UsuarioSesion("cliente@hardware.com", Roles.CLIENTE, "Forastero Perua"),
        UsuarioSesion("tecnico@hardware.com", Roles.TECNICO, "Técnico Principal")
    )

    val tecnicos = mutableStateListOf(
        Tecnico(
            id = 1,
            nombre = "Michael Charger",
            email = "michael.charger@hardware.com",
            telefono = "999-111-222",
            especialidad = "Laptops y PCs",
            activas = 2,
            completadas = 1,
            calificacion = 4.8,
            ultimaActividad = "Hoy"
        )
    )

    val adminProfile = mutableStateOf(
        AdminProfile(
            nombreCompleto = "Administrador General",
            usuario = "admin",
            correo = "admin@hardware.com",
            telefono = "999-000-111",
            rol = "Administrador",
            estado = "Activo"
        )
    )

    val equipos = mutableStateListOf(
        Equipo(
            id = 1,
            nombre = "Dell latitude 5420",
            marca = "Dell",
            nroSerie = "DL-123456",
            garantiaHasta = "30/12/2025",
            ultimoManto = "14/06/2025",
            estado = "Activo",
            historial = listOf(
                ReparacionHistorica("10/01/2025", "Técnico Juan", 150.0, "Limpieza interna"),
                ReparacionHistorica("14/06/2025", "Técnico Maria", 300.0, "Cambio de pasta térmica")
            )
        ),
        Equipo(
            id = 2,
            nombre = "Apple Macbook Pro M2",
            marca = "Apple",
            nroSerie = "MPB-98765",
            garantiaHasta = "19/05/2025",
            ultimoManto = "30/09/2025",
            estado = "En Reparacion",
            historial = listOf(
                ReparacionHistorica("15/03/2025", "Técnico Juan", 500.0, "Reparación de teclado")
            )
        )
    )

    val ordenes = mutableStateListOf(
        OrdenServicio(
            numero = 1,
            tipo = "Mantenimiento preventivo",
            equipo = "Dell latitude 5420",
            nroSerie = "DL-123456",
            estado = "completado",
            prioridad = "Baja",
            actualizado = "09/05/2026",
            tecnico = "Técnico Juan",
            fechaCreacion = "01/05/2026",
            fechaGarantia = "09/06/2026",
            fechaCompletado = "09/05/2026",
            costo = 250.0
        ),
        OrdenServicio(
            numero = 2,
            tipo = "Mantenimiento preventivo",
            equipo = "Apple Macbook Pro M2",
            nroSerie = "MPB-98765",
            estado = "pendiente",
            prioridad = "Alta",
            actualizado = "11/05/2026",
            fechaCreacion = "10/05/2026"
        ),
        OrdenServicio(
            numero = 3,
            tipo = "Reparación de pantalla",
            equipo = "HP Pavilion",
            nroSerie = "HP-777",
            estado = "en proceso",
            prioridad = "Media",
            actualizado = "15/05/2026",
            tecnico = "Técnico Maria",
            fechaCreacion = "14/05/2026"
        )
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


