package com.asaf.basic.basic

import androidx.compose.runtime.Composable

// Simple expect/actual shim to integrate with browser history on web targets.
// On non-web platforms these are no-ops.
@Composable
expect fun WebHistoryListener(onRouteChange: (String?) -> Unit)

expect fun webHistorySupported(): Boolean
expect fun webHistoryPush(route: String)
expect fun webHistoryReplace(route: String)
expect fun webHistoryBack()
expect fun webHistoryCurrentRoute(): String?
