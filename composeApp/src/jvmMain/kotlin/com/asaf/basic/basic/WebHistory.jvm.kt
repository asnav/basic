package com.asaf.basic.basic

import androidx.compose.runtime.Composable

@Composable
actual fun WebHistoryListener(onRouteChange: (String?) -> Unit) {
    // No-op on Desktop JVM
}

actual fun webHistorySupported(): Boolean = false
actual fun webHistoryPush(route: String) {}
actual fun webHistoryReplace(route: String) {}
actual fun webHistoryBack() {}
actual fun webHistoryCurrentRoute(): String? = null
