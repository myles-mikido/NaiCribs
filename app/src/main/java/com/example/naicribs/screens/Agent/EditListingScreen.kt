package com.example.naicribs.screens.Agent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.naicribs.data.AddListingViewModel
import com.example.naicribs.data.AgentViewModel
import com.example.naicribs.data.ListingRepository
import com.example.naicribs.screens.common.NaiCribsButton
import com.example.naicribs.screens.common.NaiCribsCard
import com.example.naicribs.screens.common.NaiCribsErrorBanner
import com.example.naicribs.screens.common.NaiCribsSectionLabel
import com.example.naicribs.screens.common.NaiCribsTextField
import com.example.naicribs.screens.common.NaiCribsTopBar
import com.example.naicribs.ui.theme.NaiCribsColors
import kotlinx.coroutines.launch

@Composable
fun EditListingScreen(
    navController: NavController,
    apartmentId: String,
    agentViewModel: AgentViewModel,
    editViewModel: AddListingViewModel,
) {
    val scope = rememberCoroutineScope()
    val repo = remember { ListingRepository() }

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var houseType by remember { mutableStateOf("1 Bedroom") }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var typeExpanded by remember { mutableStateOf(false) }

    val houseTypes = listOf("Bedsitter", "1 Bedroom", "2 Bedroom", "3 Bedroom", "4 Bedroom+")

    LaunchedEffect(apartmentId) {
        val apt = agentViewModel.state.value.listings.find { it.id == apartmentId }
        if (apt != null) {
            name = apt.name
            description = apt.description
            price = apt.price.toLong().toString()
            location = apt.location
            contact = apt.contactNumber
            email = apt.email
            houseType = apt.houseType.ifBlank { "1 Bedroom" }
        }
        isLoading = false
    }

    Scaffold(
        topBar = { NaiCribsTopBar(title = "Edit Listing", onBackClick = { navController.popBackStack() }) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center,
            ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
            return@Scaffold
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize().padding(padding)) {
            val isWideScreen = maxWidth > 600.dp
            val contentModifier = if (isWideScreen) Modifier.widthIn(max = 640.dp) else Modifier.fillMaxWidth()

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = contentModifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    NaiCribsErrorBanner(errorMsg)

                    NaiCribsCard {
                        NaiCribsSectionLabel("Basic details")
                        Spacer(Modifier.height(12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            var showPass = false
                            NaiCribsTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = "Apartment name",
                                leadingIcon = Icons.Default.Home,
                                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation()
                            ) {
                                IconButton(onClick = { showPass = !showPass }) {
                                    Icon(
                                        if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null, tint = NaiCribsColors.TextMuted
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = description,
                                onValueChange = { description = it },
                                label = { Text("Description") },
                                leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null, modifier = Modifier.size(20.dp)) },
                                minLines = 3,
                                maxLines = 5,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                                    cursorColor = MaterialTheme.colorScheme.primary,
                                ),
                            )

                            NaiCribsTextField(
                                value = price,
                                onValueChange = { price = it },
                                label = "Price (KSh)",
                                leadingIcon = Icons.Default.AttachMoney,
                                keyboardType = KeyboardType.Number,
                                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                            ) {
                                IconButton(onClick = { showPass = !showPass }) {
                                    Icon(
                                        if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null, tint = NaiCribsColors.TextMuted
                                    )
                                }
                            }

                            Box {
                                OutlinedTextField(
                                    value = houseType,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("House type") },
                                    trailingIcon = {
                                        IconButton(onClick = { typeExpanded = true }) {
                                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                                    ),
                                )
                                DropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                                    houseTypes.forEach { type ->
                                        DropdownMenuItem(
                                            text = { Text(type) },
                                            onClick = { houseType = type; typeExpanded = false },
                                        )
                                    }
                                }
                            }
                        }
                    }

                    NaiCribsCard {
                        NaiCribsSectionLabel("Location & contact")
                        Spacer(Modifier.height(12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            var showPass = false
                            NaiCribsTextField(
                                value = location,
                                onValueChange = { location = it },
                                label = "Location",
                                leadingIcon = Icons.Default.LocationOn,
                                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation()
                            ) {
                                IconButton(onClick = { var showPass = !showPass }) {
                                    Icon(
                                        if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null, tint = NaiCribsColors.TextMuted
                                    )
                                }
                            }
                            NaiCribsTextField(
                                value = contact,
                                onValueChange = { contact = it },
                                label = "Contact number",
                                leadingIcon = Icons.Default.Phone,
                                keyboardType = KeyboardType.Phone,
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
                                value = email,
                                onValueChange = { email = it },
                                label = "Email",
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
                        }
                    }

                    NaiCribsButton(
                        text = "Save changes",
                        icon = Icons.Default.Save,
                        isLoading = isSaving,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        onClick = {
                            scope.launch {
                                isSaving = true
                                errorMsg = null
                                val apt = agentViewModel.state.value.listings.find { it.id == apartmentId }
                                if (apt == null) {
                                    errorMsg = "Listing not found"
                                    isSaving = false
                                    return@launch
                                }
                                val updated = apt.copy(
                                    name = name.trim(),
                                    description = description.trim(),
                                    price = price.toDoubleOrNull() ?: apt.price,
                                    location = location.trim(),
                                    contactNumber = contact.trim(),
                                    email = email.trim(),
                                    houseType = houseType,
                                )
                                val result = repo.updateListing(updated)
                                if (result.isSuccess) {
                                    agentViewModel.loadMyListings()
                                    navController.popBackStack()
                                } else {
                                    errorMsg = result.exceptionOrNull()?.message ?: "Update failed"
                                }
                                isSaving = false
                            }
                        },
                    )

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}