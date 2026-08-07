package com.example.naicribs.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.naicribs.data.AuthViewModel
import com.example.naicribs.screens.common.NaiCribsButton
import com.example.naicribs.screens.common.NaiCribsErrorBanner
import com.example.naicribs.screens.common.NaiCribsTextField

@Composable
fun ForgotPasswordScreen(navController: NavController, viewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        val isWideScreen = maxWidth > 600.dp
        val contentModifier = if (isWideScreen) Modifier.widthIn(max = 440.dp) else Modifier.fillMaxWidth()

        Column(
            modifier = contentModifier.padding(24.dp).align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(64.dp))
            Spacer(Modifier.height(16.dp))
            Text("Reset Password", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground)
            Spacer(Modifier.height(8.dp))
            Text(
                "Enter your email address and we'll send you a link to reset your password.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(28.dp))

            if (state.isSuccess) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        "✅ Reset link sent! Check your inbox.",
                        modifier = Modifier.padding(14.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

            NaiCribsErrorBanner(state.errorMessage, modifier = Modifier.padding(bottom = 12.dp))

            NaiCribsTextField(
                value = email, onValueChange = { email = it },
                label = "Email address", leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
            )
            Spacer(Modifier.height(16.dp))

            NaiCribsButton(
                text = "Send reset link",
                isLoading = state.isLoading,
                onClick = { viewModel.resetPassword(email) },
            )

            Spacer(Modifier.height(14.dp))
            TextButton(onClick = { navController.popBackStack() }) {
                Text("← Back to Login", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}