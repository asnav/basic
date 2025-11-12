package com.asaf.basic.basic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import java.awt.KeyboardFocusManager
import java.awt.Toolkit
import java.awt.event.KeyEvent

@Composable
actual fun ForwardHandler(enabled: Boolean, onForward: () -> Unit) {
    val currentOnForward by rememberUpdatedState(onForward)

    DisposableEffect(enabled) {
        if (!enabled) return@DisposableEffect onDispose { }

        // Use the OS-native menu shortcut modifier (Command on macOS, Ctrl on Windows/Linux)
        val menuShortcutMask = Toolkit.getDefaultToolkit().menuShortcutKeyMaskEx

        val dispatcher = java.awt.KeyEventDispatcher { e ->
            if (e.id == KeyEvent.KEY_PRESSED) {
                val hasShortcut = (e.modifiersEx and menuShortcutMask) != 0
                if (hasShortcut && e.keyCode == KeyEvent.VK_Y) {
                    currentOnForward()
                    true
                } else false
            } else false
        }
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher)

        onDispose {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher)
        }
    }
}
