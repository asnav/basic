package com.asaf.basic.basic.state

// Public screen model so feature modules/screens can reference it if needed
sealed class Screen {
    data object Login : Screen()
    data class Home(val user: String) : Screen()
    data class Second(val user: String) : Screen()
}
