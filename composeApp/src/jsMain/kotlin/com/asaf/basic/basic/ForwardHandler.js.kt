package com.asaf.basic.basic

import androidx.compose.runtime.Composable

@Composable
actual fun ForwardHandler(enabled: Boolean, onForward: () -> Unit) {
    // No-op on web: browser forward is handled via WebHistoryListener/App routing.
}
