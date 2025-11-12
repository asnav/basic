package com.asaf.basic.basic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState

private object NavigationStoreAndroid {
    val listeners = mutableSetOf<(String?) -> Unit>()
    private val backStack = mutableListOf<String>()
    private val forwardStack = mutableListOf<String>()

    fun current(): String? = backStack.lastOrNull()

    private fun notifyAllListeners() {
        val cur = current()
        listeners.toList().forEach { it(cur) }
    }

    fun push(route: String) {
        // Push new route and clear forward stack (like web browser)
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
        // Replace does not touch forward stack
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
        NavigationStoreAndroid.listeners.add(current)
        // Fire initial state
        current(NavigationStoreAndroid.current())
        onDispose {
            NavigationStoreAndroid.listeners.remove(current)
        }
    }

    // Hook native back gesture/button
    BackHandler(enabled = NavigationStoreAndroid.canBack()) {
        navigationBack()
    }
    // ForwardHandler is a no-op on Android; harmless to include
    ForwardHandler(enabled = NavigationStoreAndroid.canForward()) {
        navigationForward()
    }
}

actual fun navigationPush(route: String) = NavigationStoreAndroid.push(route)
actual fun navigationReplace(route: String) = NavigationStoreAndroid.replace(route)
actual fun navigationBack() = NavigationStoreAndroid.back()
actual fun navigationForward() = NavigationStoreAndroid.forward()
actual fun navigationCurrentRoute(): String? = NavigationStoreAndroid.current()
