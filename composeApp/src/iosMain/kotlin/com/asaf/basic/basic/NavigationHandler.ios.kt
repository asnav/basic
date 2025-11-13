package com.asaf.basic.basic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState

private object NavigationStoreIos {
    val listeners = mutableSetOf<(String?) -> Unit>()
    private val stack = mutableListOf<String>()
    private var index = -1

    fun current(): String? = if (index in stack.indices) stack[index] else null

    private fun notifyAllListeners() {
        val cur = current()
        listeners.toList().forEach { it(cur) }
    }

    fun push(route: String) {
        while (stack.lastIndex > index) stack.removeAt(stack.lastIndex)
        stack.add(route)
        index = stack.lastIndex
        notifyAllListeners()
    }

    fun replace(route: String) {
        if (index == -1) {
            stack.add(route)
            index = 0
        } else {
            stack[index] = route
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
}

@Composable
actual fun NavigationListener(onRouteChange: (String?) -> Unit) {
    val current by rememberUpdatedState(onRouteChange)

    DisposableEffect(Unit) {
        NavigationStoreIos.listeners.add(current)
        current(NavigationStoreIos.current())
        onDispose { NavigationStoreIos.listeners.remove(current) }
    }

    // Native back: left-edge gesture set up in BackHandler.ios
    BackHandler(enabled = NavigationStoreIos.canBack()) {
        navigationBack()
    }
}

actual fun navigationPush(route: String) = NavigationStoreIos.push(route)
actual fun navigationReplace(route: String) = NavigationStoreIos.replace(route)
actual fun navigationBack() = NavigationStoreIos.back()
actual fun navigationForward() { /* iOS forward gesture not standard; keep no-op */ }
actual fun navigationCurrentRoute(): String? = NavigationStoreIos.current()
