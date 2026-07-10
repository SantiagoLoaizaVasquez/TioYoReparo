package com.t2.appaws14753.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class SnackbarEstado(val colorFondo: Color,val colorTexto: Color,
    val icono: ImageVector,val descripcion : String) {

    SUCCESS(Color(0xFF5FE165),Color(0xFF000000), Icons.Default.CheckCircle,""),
    ERROR(Color(0xFFD32F2F), Color(0xFFFFFFFF), Icons.Default.Error,""),
    WARNING(Color(0xFFFBC02D),Color(0xFF000000), Icons.Default.Warning,"")
}
