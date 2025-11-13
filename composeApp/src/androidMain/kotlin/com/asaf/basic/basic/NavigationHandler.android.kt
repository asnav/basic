package com.asaf.basic.basic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState

private object NavigationStoreAndroid {
    val listeners = mutableSetOf<(String?) -> Unit>()
    private val stack = mutableListOf<String>()
    private var index = -1

    fun current(): String? = if (index in stack.indices) stack[index] else null

    private fun notifyAllListeners() {
        val cur = current()
        listeners.toList().forEach { it(cur) }
    }

    fun push(route: String) {
        // Drop forward history if we are not at the end
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
    fun canForward(): Boolean = index in 0 until stack.lastIndex
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
