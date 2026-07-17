package com.example.naicribs.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.naicribs.data.AuthViewModel
import com.example.naicribs.navigation.Routes
import com.example.naicribs.screens.common.NaiCribsTextField
import com.example.naicribs.ui.theme.NaiCribsColors

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    role: String = "customer"
) {
    var fullName    by remember { mutableStateOf("") }
    var email       by remember { mutableStateOf("") }
    var password    by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var showPass    by remember { mutableStateOf(false) }
    var agreedTerms  by remember { mutableStateOf(false) }
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

    Column(
        modifier = Modifier.fillMaxSize().background(NaiCribsColors.Background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(160.dp)
                .background(Brush.verticalGradient(listOf(NaiCribsColors.PurpleDark, NaiCribsColors.Purple))),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
            ) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White) }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                Spacer(Modifier.height(6.dp))
                Text("Create Account", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(if (role == "agent") "as an Agent" else "as a Customer",
                    fontSize = 13.sp, color = Color.White.copy(alpha = 0.75f))
            }
        }

        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            state.errorMessage?.let {
                Surface(shape = RoundedCornerShape(10.dp), color = NaiCribsColors.ErrorLight) {
                    Text(it, modifier = Modifier.padding(12.dp), fontSize = 13.sp, color = NaiCribsColors.Error)
                }
            }

            NaiCribsTextField(
                value = fullName, onValueChange = { fullName = it },
                label = "Full name", leadingIcon = Icons.Default.Person, visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation()
            ) {
                IconButton(onClick = { showPass = !showPass }) {
                    Icon(
                        if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null, tint = NaiCribsColors.TextMuted
                    )
                }
            }
            NaiCribsTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email address",
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation()
            ) {
                IconButton(onClick = { showPass = !showPass }) {
                    Icon(
                        if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null, tint = NaiCribsColors.TextMuted
                    )
                }
            }
            NaiCribsTextField(
                value = password, onValueChange = { password = it },
                label = "Password", leadingIcon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                trailingContent = {
                    IconButton(onClick = { showPass = !showPass }) {
                        Icon(if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null, tint = NaiCribsColors.TextMuted)
                    }
                }
            )
            NaiCribsTextField(
                value = confirmPass, onValueChange = { confirmPass = it },
                label = "Confirm password", leadingIcon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation()
            ) {
                IconButton(onClick = { showPass = !showPass }) {
                    Icon(
                        if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null, tint = NaiCribsColors.TextMuted
                    )
                }
            }
            if (password.isNotEmpty() && confirmPass.isNotEmpty() && password != confirmPass) {
                Text("Passwords do not match", color = NaiCribsColors.Error, fontSize = 12.sp)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = agreedTerms,
                    onCheckedChange = { agreedTerms = it },
                    colors = CheckboxDefaults.colors(checkedColor = NaiCribsColors.Purple)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    buildAnnotatedString {
                        append("I agree to the ")
                        withStyle(SpanStyle(color = NaiCribsColors.Purple, fontWeight = FontWeight.SemiBold)) {
                            append("Terms & Conditions")
                        }
                    },
                    fontSize = 13.sp, color = NaiCribsColors.TextPrimary,
                    modifier = Modifier.clickable { agreedTerms = !agreedTerms }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = agreedPolicy,
                    onCheckedChange = { agreedPolicy = it },
                    colors = CheckboxDefaults.colors(checkedColor = NaiCribsColors.Purple)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    buildAnnotatedString {
                        append("I agree to the ")
                        withStyle(SpanStyle(color = NaiCribsColors.Purple, fontWeight = FontWeight.SemiBold)) {
                            append("Privacy Policy")
                        }
                    },
                    fontSize = 13.sp, color = NaiCribsColors.TextPrimary,
                    modifier = Modifier.clickable { agreedPolicy = !agreedPolicy }
                )
            }

            if ((!agreedTerms || !agreedPolicy) && fullName.isNotBlank()) {
                Text(
                    "Please agree to both Terms & Conditions and Privacy Policy to continue",
                    color = NaiCribsColors.Error, fontSize = 12.sp
                )
            }

            Button(
                onClick = {
                    if (password == confirmPass && agreedTerms && agreedPolicy) {
                        viewModel.register(email, password, fullName, role)
                    }
                },
                enabled = !state.isLoading && password == confirmPass && agreedTerms && agreedPolicy,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NaiCribsColors.Purple)
            ) {
                if (state.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                else Text("Create account", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Already have an account? ", fontSize = 13.sp, color = NaiCribsColors.TextMuted)
                Text("Sign in", fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                    color = NaiCribsColors.Purple,
                    modifier = Modifier.clickable { navController.popBackStack() })
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}