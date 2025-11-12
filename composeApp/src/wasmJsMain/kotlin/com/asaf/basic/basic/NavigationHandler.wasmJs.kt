package com.asaf.basic.basic

import androidx.compose.runtime.Composable
import kotlinx.browser.window

@Composable
actual fun NavigationListener(onRouteChange: (String?) -> Unit) {
    WebHistoryListener(onRouteChange)
}

actual fun navigationPush(route: String) { webHistoryPush(route) }
actual fun navigationReplace(route: String) { webHistoryReplace(route) }
actual fun navigationBack() { webHistoryBack() }
actual fun navigationForward() { window.history.forward() }
actual fun navigationCurrentRoute(): String? = webHistoryCurrentRoute()
