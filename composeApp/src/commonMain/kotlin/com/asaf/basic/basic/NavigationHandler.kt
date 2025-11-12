package com.asaf.basic.basic

import androidx.compose.runtime.Composable

// Unified navigation handler API across all platforms.
// On web, delegates to browser history. On other platforms, maintains an in-memory stack
// and ties into native/back-forward triggers where available.
@Composable
expect fun NavigationListener(onRouteChange: (String?) -> Unit)

expect fun navigationPush(route: String)
expect fun navigationReplace(route: String)
expect fun navigationBack()
expect fun navigationForward()
expect fun navigationCurrentRoute(): String?
