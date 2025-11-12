package com.asaf.basic.basic

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // No-op on web (WASM): handled centrally via WebHistoryListener in App().
}
