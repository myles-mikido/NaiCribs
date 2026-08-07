package com.example.naicribs.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.naicribs.data.AuthViewModel
import com.example.naicribs.navigation.Routes
import com.example.naicribs.screens.common.NaiCribsButton
import com.example.naicribs.screens.common.NaiCribsErrorBanner
import com.example.naicribs.screens.common.NaiCribsTextField

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    role: String = "customer",
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }
    var agreedTerms by remember { mutableStateOf(false) }
    var agreedPolicy by remember { mutableStateOf(false) }

    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            val destination = if (role == "agent") Routes.AgentDashboard else Routes.CustomerDashboard
            navController.navigate(destination) {
                popUpTo(Routes.RoleSelection) { inclusive = true }
            }
            viewModel.clearError()
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        val isWideScreen = maxWidth > 600.dp
        val contentModifier = if (isWideScreen) Modifier.widthIn(max = 480.dp) else Modifier.fillMaxWidth()

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(160.dp)
                    .background(
                        Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary))
                    ),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.TopStart).padding(8.dp),
                ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White) }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                    Spacer(Modifier.height(6.dp))
                    Text("Create Account", style = MaterialTheme.typography.headlineSmall, color = Color.White)
                    Text(
                        if (role == "agent") "as an Agent" else "as a Customer",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                    )
                }
            }

            Column(
                modifier = contentModifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                NaiCribsErrorBanner(state.errorMessage)

                NaiCribsTextField(value = fullName, onValueChange = { fullName = it }, label = "Full name", leadingIcon = Icons.Default.Person)
                NaiCribsTextField(value = email, onValueChange = { email = it }, label = "Email address", leadingIcon = Icons.Default.Email, keyboardType = KeyboardType.Email)
                NaiCribsTextField(
                    value = password, onValueChange = { password = it },
                    label = "Password", leadingIcon = Icons.Default.Lock,
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingContent = {
                        IconButton(onClick = { showPass = !showPass }) {
                            Icon(
                                if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    },
                )
                NaiCribsTextField(
                    value = confirmPass, onValueChange = { confirmPass = it },
                    label = "Confirm password", leadingIcon = Icons.Default.Lock,
                    keyboardType = KeyboardType.Password,
                    visualTransformation = PasswordVisualTransformation(),
                )
                if (password.isNotEmpty() && confirmPass.isNotEmpty() && password != confirmPass) {
                    Text("Passwords do not match", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Checkbox(
                        checked = agreedTerms,
                        onCheckedChange = { agreedTerms = it },
                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary),
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        buildAnnotatedString {
                            append("I agree to the ")
                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)) {
                                append("Terms & Conditions")
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.clickable { agreedTerms = !agreedTerms },
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Checkbox(
                        checked = agreedPolicy,
                        onCheckedChange = { agreedPolicy = it },
                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary),
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        buildAnnotatedString {
                            append("I agree to the ")
                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)) {
                                append("Privacy Policy")
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.clickable { agreedPolicy = !agreedPolicy },
                    )
                }

                if ((!agreedTerms || !agreedPolicy) && fullName.isNotBlank()) {
                    Text(
                        "Please agree to both Terms & Conditions and Privacy Policy to continue",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                NaiCribsButton(
                    text = "Create account",
                    isLoading = state.isLoading,
                    enabled = password == confirmPass && agreedTerms && agreedPolicy,
                    onClick = {
                        if (password == confirmPass && agreedTerms && agreedPolicy) {
                            viewModel.register(email, password, fullName, role)
                        }
                    },
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(
                        "Already have an account? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        "Sign in",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { navController.popBackStack() },
                    )
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}