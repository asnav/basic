package com.asaf.basic.basic

import androidx.compose.runtime.Composable

@Composable
actual fun ForwardHandler(enabled: Boolean, onForward: () -> Unit) {
    // No-op on web (WASM): forward is controlled by the browser's history and WebHistoryListener.
}
