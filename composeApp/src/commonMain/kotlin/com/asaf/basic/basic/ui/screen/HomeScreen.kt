package com.asaf.basic.basic.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    user: String,
    onLogout: () -> Unit,
    onNavigateSecond: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.height(1.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Home", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(12.dp))
            Text(text = "Hello, $user 👋", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(24.dp))
            Text(text = "You are now authenticated. This is a placeholder home screen.")
            Spacer(Modifier.height(24.dp))
            Button(onClick = onNavigateSecond) { Text("Go to second screen") }
        }
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text("Log out")
        }
    }
}
