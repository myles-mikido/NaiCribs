package com.example.naicribs.screens.agent

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.naicribs.data.AddListingViewModel
import com.example.naicribs.navigation.Routes
import com.example.naicribs.screens.common.NaiCribsButton
import com.example.naicribs.screens.common.NaiCribsCard
import com.example.naicribs.screens.common.NaiCribsErrorBanner
import com.example.naicribs.screens.common.NaiCribsSectionLabel
import com.example.naicribs.screens.common.NaiCribsTextField
import com.example.naicribs.screens.common.NaiCribsTopBar
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.naicribs.ui.theme.NaiCribsColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddListingScreen(
    navController: NavController,
    viewModel: AddListingViewModel,
    showPass: Boolean
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var houseType by remember { mutableStateOf("1 Bedroom") }
    var typeExpanded by remember { mutableStateOf(false) }

    var tagInput by remember { mutableStateOf("") }
    var tagList by remember { mutableStateOf(listOf<String>()) }

    val houseTypes = listOf("Bedsitter", "1 Bedroom", "2 Bedroom", "3 Bedroom", "4 Bedroom+")
    val state by viewModel.state.collectAsState()

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val pickedLat by (savedStateHandle?.getStateFlow("picked_lat", 0.0) ?: return).collectAsState()
    val pickedLng by (savedStateHandle?.getStateFlow("picked_lng", 0.0) ?: return).collectAsState()
    val pickedLabel by (savedStateHandle?.getStateFlow("picked_label", "") ?: return).collectAsState()

    LaunchedEffect(pickedLat, pickedLng) {
        if (pickedLat != 0.0 && pickedLng != 0.0) {
            viewModel.setLocation(pickedLat, pickedLng, pickedLabel)
            location = pickedLabel.ifBlank { "%.4f, %.4f".format(pickedLat, pickedLng) }
        }
    }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { viewModel.uploadImage(context, it) }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.navigate(Routes.MyListings) {
                popUpTo(Routes.AddListing) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = { NaiCribsTopBar(title = "New Listing", onBackClick = { navController.popBackStack() }) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
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
                    NaiCribsErrorBanner(state.errorMessage)

                    // Basic info
                    NaiCribsCard {
                        NaiCribsSectionLabel("Basic details")
                        Spacer(Modifier.height(12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            var showPass = false
                            NaiCribsTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = "Apartment name *",
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
                                value = description, onValueChange = { description = it },
                                label = { Text("Description") },
                                leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null, modifier = Modifier.size(20.dp)) },
                                minLines = 3, maxLines = 5, modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                                    cursorColor = MaterialTheme.colorScheme.primary,
                                ),
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(modifier = Modifier.weight(1f)) {
                                    NaiCribsTextField(
                                        value = price,
                                        onValueChange = { price = it },
                                        label = "Price (KSh) *",
                                        leadingIcon = Icons.Default.AttachMoney,
                                        keyboardType = KeyboardType.Number,
                                        visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation()
                                    ) {
                                        IconButton(onClick = { showPass = !showPass }) {
                                            Icon(
                                                if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                contentDescription = null,
                                                tint = NaiCribsColors.TextMuted
                                            )
                                        }
                                    }
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    OutlinedTextField(
                                        value = houseType, onValueChange = {}, readOnly = true,
                                        label = { Text("House type") },
                                        trailingIcon = {
                                            IconButton(onClick = { typeExpanded = !typeExpanded }) {
                                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                                        ),
                                    )
                                    DropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                                        houseTypes.forEach { type ->
                                            DropdownMenuItem(text = { Text(type) }, onClick = { houseType = type; typeExpanded = false })
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Location
                    NaiCribsCard {
                        NaiCribsSectionLabel("Location")
                        Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                var showPass = false
                                NaiCribsTextField(
                                    value = location,
                                    onValueChange = { location = it },
                                    label = "Location / Area *",
                                    leadingIcon = Icons.Default.LocationOn,
                                    visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation()
                                ) {
                                    IconButton(onClick = {
                                        var showPass = false
                                        showPass = !showPass
                                    }) {
                                        Icon(
                                            if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = null,
                                            tint = NaiCribsColors.TextMuted
                                        )
                                    }
                                }
                            }
                            FilledIconButton(
                                onClick = { navController.navigate(Routes.LocationPicker) },
                                modifier = Modifier.size(56.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                ),
                            ) { Icon(Icons.Default.Map, contentDescription = "Pin on map") }
                        }
                        AnimatedVisibility(
                            visible = state.pinnedLatitude != null,
                            enter = fadeIn(tween(200)),
                            exit = fadeOut(tween(150)),
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.padding(top = 10.dp),
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text("Location pinned on map", style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer)
                                }
                            }
                        }
                    }

                    // Tags
                    NaiCribsCard {
                        NaiCribsSectionLabel("Area tags")
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Add searchable tags e.g. Westlands, Near CBD, Gated",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = tagInput,
                                onValueChange = { tagInput = it },
                                label = { Text("Add tag") },
                                leadingIcon = { Icon(Icons.Default.Tag, contentDescription = null, modifier = Modifier.size(18.dp)) },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                                ),
                            )
                            FilledTonalButton(
                                onClick = {
                                    val tag = tagInput.trim()
                                    if (tag.isNotBlank() && tag !in tagList) {
                                        tagList = tagList + tag
                                        tagInput = ""
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                            ) { Text("Add") }
                        }
                        AnimatedVisibility(visible = tagList.isNotEmpty()) {
                            FlowRow(
                                modifier = Modifier.padding(top = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                tagList.forEach { tag ->
                                    AssistChip(
                                        onClick = { tagList = tagList - tag },
                                        label = { Text(tag) },
                                        trailingIcon = { Icon(Icons.Default.Close, contentDescription = "Remove tag", modifier = Modifier.size(14.dp)) },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                                            labelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                            trailingIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        ),
                                        border = null,
                                    )
                                }
                            }
                        }
                    }

                    // Contact
                    NaiCribsCard {
                        NaiCribsSectionLabel("Contact info")
                        Spacer(Modifier.height(12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            NaiCribsTextField(
                                value = contact,
                                onValueChange = { contact = it },
                                label = "Contact number *",
                                leadingIcon = Icons.Default.Phone,
                                keyboardType = KeyboardType.Phone,
                                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation()
                            ) {
                                IconButton(onClick = { var showPass = false
                                    showPass = !showPass }) {
                                    Icon(
                                        if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null, tint = NaiCribsColors.TextMuted
                                    )
                                }
                            }
                            NaiCribsTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = "Email (optional)",
                                leadingIcon = Icons.Default.Email,
                                keyboardType = KeyboardType.Email,
                                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation()
                            ) {
                                IconButton(onClick = { var showPass = false
                                    showPass = !showPass }) {
                                    Icon(
                                        if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null, tint = NaiCribsColors.TextMuted
                                    )
                                }
                            }
                        }
                    }

                    // Photos
                    NaiCribsCard {
                        NaiCribsSectionLabel("Photos")
                        Spacer(Modifier.height(12.dp))
                        AnimatedVisibility(visible = state.uploadedImageUrls.isNotEmpty()) {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(bottom = 12.dp)) {
                                items(state.uploadedImageUrls) { url ->
                                    Box {
                                        AsyncImage(
                                            model = url, contentDescription = null, contentScale = ContentScale.Crop,
                                            modifier = Modifier.size(84.dp).clip(RoundedCornerShape(14.dp))
                                                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), RoundedCornerShape(14.dp)),
                                        )
                                        IconButton(
                                            onClick = { viewModel.removeImage(url) },
                                            modifier = Modifier.size(24.dp).align(Alignment.TopEnd)
                                                .background(MaterialTheme.colorScheme.error, CircleShape),
                                        ) {
                                            Icon(Icons.Default.Close, contentDescription = "Remove", tint = MaterialTheme.colorScheme.onError, modifier = Modifier.size(14.dp))
                                        }
                                    }
                                }
                            }
                        }
                        OutlinedButton(
                            onClick = { imagePicker.launch("image/*") },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                        ) {
                            if (state.isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = MaterialTheme.colorScheme.primary, strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.AddPhotoAlternate, null, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Upload photos", fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    NaiCribsButton(
                        text = "Submit listing",
                        icon = Icons.Default.Check,
                        isLoading = state.isLoading,
                        onClick = { viewModel.submitListing(name, description, price, location, contact, email, houseType, tagList) },
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}