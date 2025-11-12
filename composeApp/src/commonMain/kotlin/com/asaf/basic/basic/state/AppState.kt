package com.asaf.basic.basic.state

import androidx.compose.runtime.*
import com.asaf.basic.basic.NavigationListener
import com.asaf.basic.basic.navigationBack
import com.asaf.basic.basic.navigationCurrentRoute
import com.asaf.basic.basic.navigationForward
import com.asaf.basic.basic.navigationPush
import com.asaf.basic.basic.navigationReplace

// Public screen model so feature modules/screens can reference it if needed
sealed class Screen {
    data object Login : Screen()
    data class Home(val user: String) : Screen()
    data class Second(val user: String) : Screen()
}

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
        navigationPush("home")
    }

    fun logout() {
        screen = Screen.Login
        navigationReplace("login")
    }

    fun toSecond() {
        val u = when (val s = screen) {
            is Screen.Home -> s.user
            is Screen.Second -> s.user
            Screen.Login -> lastUser
        } ?: return
        screen = Screen.Second(u)
        navigationPush("second")
    }

    fun backFromSecond() {
        navigationBack()
    }

    fun forward() {
        navigationForward()
    }
}

@Composable
fun rememberAppState(): AppState {
    val screenState = remember { mutableStateOf<Screen>(Screen.Login) }
    val lastUserState = remember { mutableStateOf<String?>(null) }

    // Listen to unified navigation and map routes to screens
    NavigationListener { route ->
        when (route) {
            "login" -> {
                screenState.value = Screen.Login
            }
            "home" -> {
                val u = lastUserState.value
                screenState.value = if (u != null) Screen.Home(u) else Screen.Login
            }
            "second" -> {
                val u = lastUserState.value
                screenState.value = if (u != null) Screen.Second(u) else Screen.Login
            }
            else -> Unit
        }
    }

    // Ensure an initial route exists for correct forward/back behavior.
    DisposableEffect(Unit) {
        if (navigationCurrentRoute() == null) {
            navigationReplace("login")
        }
        onDispose { }
    }

    return remember { AppState(screenState, lastUserState) }
}
