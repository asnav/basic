package com.asaf.basic.basic

import androidx.compose.runtime.Composable

@Composable
actual fun ForwardHandler(enabled: Boolean, onForward: () -> Unit) {
    // No-op on iOS; native navigation exposes only back in typical stacks.
}
