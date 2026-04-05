package com.asaf.basic.basic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import com.asaf.basic.basic.state.Screen

private object NavigationStore {
    val listeners = mutableSetOf<(Screen?) -> Unit>()
    private val stack = mutableListOf<Screen>()
    private var index = -1

    fun current(): Screen? = if (index in stack.indices) stack[index] else null

    private fun notifyAllListeners() {
        val cur = current()
        listeners.toList().forEach { it(cur) }
    }

    fun push(screen: Screen) {
        while (stack.lastIndex > index) stack.removeAt(stack.lastIndex)
        stack.add(screen)
        index = stack.lastIndex
        notifyAllListeners()
    }

    fun replace(screen: Screen) {
        if (index == -1) {
            stack.add(screen)
            index = 0
        } else {
            stack[index] = screen
        }
        notifyAllListeners()
    }

    fun back() {
        if (index > 0) {
            index -= 1
            notifyAllListeners()
        }
    }

    fun forward() {
        if (index < stack.lastIndex) {
            index += 1
            notifyAllListeners()
        }
    }

    fun canBack(): Boolean = index > 0
    fun canForward(): Boolean = index in 0 until stack.lastIndex
}

actual object Navigation {
    @Composable
    actual fun addListener(onScreenChange: (Screen?) -> Unit) {
        val current by rememberUpdatedState(onScreenChange)

        DisposableEffect(Unit) {
            NavigationStore.listeners.add(current)
            current(NavigationStore.current())
            onDispose { NavigationStore.listeners.remove(current) }
        }

        BackHandler(enabled = NavigationStore.canBack()) { back() }
        ForwardHandler(enabled = NavigationStore.canForward()) { forward() }
    }

    actual fun push(screen: Screen) = NavigationStore.push(screen)
    actual fun replace(screen: Screen) = NavigationStore.replace(screen)
    actual fun back() = NavigationStore.back()
    actual fun forward() = NavigationStore.forward()
    actual fun current(): Screen? = NavigationStore.current()
}
