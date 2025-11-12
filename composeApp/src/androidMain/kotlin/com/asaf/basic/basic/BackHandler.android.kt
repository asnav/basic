package com.asaf.basic.basic

import androidx.activity.compose.BackHandler as AndroidBackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    AndroidBackHandler(enabled = enabled, onBack = onBack)
}

@Composable
actual fun ForwardHandler(enabled: Boolean, onForward: () -> Unit) {
    // No-op on Android; forward via keyboard is not a native pattern for back stack.
}
