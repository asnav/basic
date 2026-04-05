package com.asaf.basic.basic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.browser.window
import org.w3c.dom.PopStateEvent
import org.w3c.dom.events.EventListener

private fun buildState(route: String): dynamic {
    val o = js("({})")
    o.route = route
    return o
}

@Composable
fun WebHistoryListener(onRouteChange: (String?) -> Unit) {
    val current = rememberUpdatedState(onRouteChange)
    DisposableEffect(Unit) {
        val listener = EventListener { ev ->
            val pe = ev as? PopStateEvent
            val route = try {
                val s = pe?.state
                if (s == null) null else (s.asDynamic().route as? String)
            } catch (_: dynamic) {
                null
            }
            current.value(route)
        }
        window.addEventListener("popstate", listener)
        onDispose {
            window.removeEventListener("popstate", listener)
        }
    }
}

fun webHistorySupported(): Boolean = true

fun webHistoryPush(route: String) {
    try {
        window.history.pushState(buildState(route), "")
    } catch (_: dynamic) {
        // ignore
    }
}

fun webHistoryReplace(route: String) {
    try {
        window.history.replaceState(buildState(route), "")
    } catch (_: dynamic) {
        // ignore
    }
}

fun webHistoryBack() {
    window.history.back()
}

fun webHistoryCurrentRoute(): String? {
    return try {
        val s = window.history.state
        if (s == null) null else (s.asDynamic().route as? String)
    } catch (_: dynamic) {
        null
    }
}
