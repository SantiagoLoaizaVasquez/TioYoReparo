package com.t2.appaws14753.presentation.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

data class SnackbarPersonalizado(
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val message: String,
    override val withDismissAction: Boolean = false,
    val estado : SnackbarEstado
): SnackbarVisuals
