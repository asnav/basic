package com.asaf.basic.basic

import androidx.compose.runtime.Composable
import com.asaf.basic.basic.state.Screen

// Unified navigation API encapsulated in a single object.
// listener notifies with Screen?; push/replace/back/forward operate on Screen.
expect object Navigation {
    @Composable
    fun addListener(onScreenChange: (Screen?) -> Unit)

    fun push(screen: Screen)
    fun replace(screen: Screen)
    fun back()
    fun forward()
    fun current(): Screen?
}
