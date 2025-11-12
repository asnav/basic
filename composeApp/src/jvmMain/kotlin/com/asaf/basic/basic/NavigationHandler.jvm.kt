package com.asaf.basic.basic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState

private object NavigationStoreJvm {
    val listeners = mutableSetOf<(String?) -> Unit>()
    private val backStack = mutableListOf<String>()
    private val forwardStack = mutableListOf<String>()

    fun current(): String? = backStack.lastOrNull()

    private fun notifyAllListeners() {
        val cur = current()
        listeners.toList().forEach { it(cur) }
    }

    fun push(route: String) {
        backStack.add(route)
        forwardStack.clear()
        notifyAllListeners()
    }

    fun replace(route: String) {
        if (backStack.isEmpty()) {
            backStack.add(route)
        } else {
            backStack[backStack.lastIndex] = route
        }
        notifyAllListeners()
    }

    fun back() {
        if (backStack.size > 1) {
            val removed = backStack.removeAt(backStack.lastIndex)
            forwardStack.add(removed)
            notifyAllListeners()
        }
    }

    fun forward() {
        if (forwardStack.isNotEmpty()) {
            val next = forwardStack.removeAt(forwardStack.lastIndex)
            backStack.add(next)
            notifyAllListeners()
        }
    }

    fun canBack(): Boolean = backStack.size > 1
    fun canForward(): Boolean = forwardStack.isNotEmpty()
}

@Composable
actual fun NavigationListener(onRouteChange: (String?) -> Unit) {
    val current by rememberUpdatedState(onRouteChange)

    DisposableEffect(Unit) {
        NavigationStoreJvm.listeners.add(current)
        current(NavigationStoreJvm.current())
        onDispose { NavigationStoreJvm.listeners.remove(current) }
    }

    BackHandler(enabled = NavigationStoreJvm.canBack()) { navigationBack() }
    ForwardHandler(enabled = NavigationStoreJvm.canForward()) { navigationForward() }
}

actual fun navigationPush(route: String) = NavigationStoreJvm.push(route)
actual fun navigationReplace(route: String) = NavigationStoreJvm.replace(route)
actual fun navigationBack() = NavigationStoreJvm.back()
actual fun navigationForward() = NavigationStoreJvm.forward()
actual fun navigationCurrentRoute(): String? = NavigationStoreJvm.current()
