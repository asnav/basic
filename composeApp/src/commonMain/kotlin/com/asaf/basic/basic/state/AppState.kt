package com.asaf.basic.basic.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.asaf.basic.basic.Navigation

class AppState internal constructor(
    screenState: MutableState<Screen>,
    private val lastUserState: MutableState<String?>
) {
    var screen: Screen by screenState
    private var lastUser: String? by lastUserState

    // Expose helpers to drive navigation from UI layer
    fun onLoginSuccess(user: String) {
        lastUser = user
        screen = Screen.Home(user)
        Navigation.push(Screen.Home(user))
    }

    fun logout() {
        screen = Screen.Login
        Navigation.replace(Screen.Login)
    }

    fun toSecond() {
        val u = when (val s = screen) {
            is Screen.Home -> s.user
            is Screen.Second -> s.user
            Screen.Login -> lastUser
        } ?: return
        val next = Screen.Second(u)
        screen = next
        Navigation.push(next)
    }

    fun backFromSecond() {
        Navigation.back()
    }

    fun forward() {
        Navigation.forward()
    }
}

@Composable
fun rememberAppState(): AppState {
    val screenState = remember { mutableStateOf<Screen>(Screen.Login) }
    val lastUserState = remember { mutableStateOf<String?>(null) }

    // Listen to unified navigation and map to screens directly
    Navigation.addListener { screen ->
        if (screen != null) {
            screenState.value = when (screen) {
                Screen.Login -> Screen.Login
                is Screen.Home -> screen.apply { lastUserState.value = user }
                is Screen.Second -> screen.apply { lastUserState.value = user }
            }
        }
    }

    // Ensure an initial route exists for correct forward/back behavior.
    DisposableEffect(Unit) {
        if (Navigation.current() == null) {
            Navigation.replace(Screen.Login)
        }
        onDispose { }
    }

    return remember { AppState(screenState, lastUserState) }
}
