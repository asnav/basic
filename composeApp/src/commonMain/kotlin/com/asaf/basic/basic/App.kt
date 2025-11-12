package com.asaf.basic.basic

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.asaf.basic.basic.state.Screen
import com.asaf.basic.basic.state.rememberAppState
import com.asaf.basic.basic.ui.screen.HomeScreen
import com.asaf.basic.basic.ui.screen.LoginScreen
import com.asaf.basic.basic.ui.screen.SecondScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
        val appState = rememberAppState()

        Surface(modifier = Modifier.fillMaxSize()) {
            when (val s = appState.screen) {
                Screen.Login -> LoginScreen(
                    onLoginSuccess = { user -> appState.onLoginSuccess(user) }
                )
                is Screen.Home -> {
                    HomeScreen(
                        user = s.user,
                        onLogout = { appState.logout() },
                        onNavigateSecond = { appState.toSecond() }
                    )
                }
                is Screen.Second -> SecondScreen(
                    user = s.user,
                    onBack = { appState.backFromSecond() }
                )
            }
        }
    }
}
