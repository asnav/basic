package com.asaf.basic.basic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.browser.window

@Composable
actual fun WebHistoryListener(onRouteChange: (String?) -> Unit) {
    val current = rememberUpdatedState(onRouteChange)
    DisposableEffect(Unit) {
        val hashListener: (org.w3c.dom.events.Event) -> Unit = {
            val route = currentHashRoute()
            current.value(route)
        }
        window.addEventListener(type = "hashchange", callback = hashListener)
        onDispose {
            window.removeEventListener(type = "hashchange", callback = hashListener)
        }
    }
}

private fun currentHashRoute(): String? {
    val hash = window.location.hash
    return hash.removePrefix("#/").ifBlank { null }
}

actual fun webHistorySupported(): Boolean = true

actual fun webHistoryPush(route: String) {
    // Update the hash; this creates a new history entry and enables forward/back.
    window.location.hash = "/$route"
}

actual fun webHistoryReplace(route: String) {
    // Replace current history entry's hash without adding a new entry.
    runCatching { window.history.replaceState(data = null, title = "", url = "#/$route") }
}

actual fun webHistoryBack() {
    window.history.back()
}

actual fun webHistoryCurrentRoute(): String? = currentHashRoute()
