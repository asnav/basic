package com.asaf.basic.basic

import androidx.compose.runtime.Composable

// Expect/actual shim to handle native back on platforms that support it (Android)
@Composable
expect fun BackHandler(enabled: Boolean = true, onBack: () -> Unit)

// Forward handler for platforms that support a native/standard "go forward" action (e.g., Command/Ctrl+Y on Desktop JVM).
@Composable
expect fun ForwardHandler(enabled: Boolean = true, onForward: () -> Unit)
