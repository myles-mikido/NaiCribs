package com.example.naicribs.screens.agent

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.naicribs.data.AddListingViewModel
import com.example.naicribs.navigation.Routes
import com.example.naicribs.screens.common.NaiCribsTextField
import com.example.naicribs.screens.common.NaiCribsTopBar
import com.example.naicribs.ui.theme.NaiCribsColors
import androidx.compose.foundation.layout.FlowRow

@Composable
fun AddListingScreen(navController: NavController, viewModel: AddListingViewModel) {
    val context = LocalContext.current

    var name         by remember { mutableStateOf("") }
    var description  by remember { mutableStateOf("") }
    var price        by remember { mutableStateOf("") }
    var location     by remember { mutableStateOf("") }
    var contact      by remember { mutableStateOf("") }
    var email        by remember { mutableStateOf("") }
    var houseType    by remember { mutableStateOf("1 Bedroom") }
    var typeExpanded by remember { mutableStateOf(false) }

    // Tags
    var tagInput  by remember { mutableStateOf("") }
    var tagList   by remember { mutableStateOf(listOf<String>()) }

    val houseTypes = listOf("Bedsitter", "1 Bedroom", "2 Bedroom", "3 Bedroom", "4 Bedroom+")
    val state by viewModel.state.collectAsState()

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val pickedLat   by (savedStateHandle?.getStateFlow("picked_lat",   0.0) ?: return).collectAsState()
    val pickedLng   by (savedStateHandle?.getStateFlow("picked_lng",   0.0) ?: return).collectAsState()
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
        topBar = { NaiCribsTopBar(title = "New Listing", onBackClick = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().background(NaiCribsColors.Background)
                .padding(padding).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            state.errorMessage?.let {
                Surface(shape = RoundedCornerShape(10.dp), color = NaiCribsColors.ErrorLight) {
                    Text(it, modifier = Modifier.padding(12.dp), fontSize = 13.sp, color = NaiCribsColors.Error)
                }
            }

            NaiCribsTextField(value = name, onValueChange = { name = it },
                label = "Apartment name *", leadingIcon = Icons.Default.Home)

            OutlinedTextField(
                value = description, onValueChange = { description = it },
                label = { Text("Description", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null, tint = NaiCribsColors.TextMuted, modifier = Modifier.size(20.dp)) },
                minLines = 3, maxLines = 5, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NaiCribsColors.Purple, unfocusedBorderColor = NaiCribsColors.Border,
                    focusedLabelColor = NaiCribsColors.Purple, cursorColor = NaiCribsColors.Purple
                )
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    NaiCribsTextField(value = price, onValueChange = { price = it },
                        label = "Price (KSh) *", leadingIcon = Icons.Default.AttachMoney, keyboardType = KeyboardType.Number)
                }
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = houseType, onValueChange = {}, readOnly = true,
                        label = { Text("House type", fontSize = 12.sp) },
                        trailingIcon = {
                            IconButton(onClick = { typeExpanded = !typeExpanded }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = NaiCribsColors.TextMuted)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NaiCribsColors.Purple, unfocusedBorderColor = NaiCribsColors.Border, focusedLabelColor = NaiCribsColors.Purple
                        )
                    )
                    DropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                        houseTypes.forEach { type ->
                            DropdownMenuItem(text = { Text(type) }, onClick = { houseType = type; typeExpanded = false })
                        }
                    }
                }
            }


            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    NaiCribsTextField(value = location, onValueChange = { location = it },
                        label = "Location / Area *", leadingIcon = Icons.Default.LocationOn)
                }
                IconButton(
                    onClick = { navController.navigate(Routes.LocationPicker) },
                    modifier = Modifier.size(52.dp).clip(RoundedCornerShape(12.dp)).background(NaiCribsColors.PurpleLight)
                ) { Icon(Icons.Default.Map, contentDescription = "Pin on map", tint = NaiCribsColors.Purple) }
            }
            if (state.pinnedLatitude != null) {
                Surface(shape = RoundedCornerShape(8.dp), color = NaiCribsColors.TealLight) {
                    Text("📍 Location pinned on map", modifier = Modifier.padding(10.dp), fontSize = 12.sp, color = NaiCribsColors.TealDark)
                }
            }


            Text("Area Tags", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.TextPrimary)
            Text("Add searchable tags e.g. Westlands, Near CBD, Gated", fontSize = 12.sp, color = NaiCribsColors.TextMuted)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = tagInput,
                    onValueChange = { tagInput = it },
                    label = { Text("Add tag", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Tag, contentDescription = null, tint = NaiCribsColors.TextMuted, modifier = Modifier.size(18.dp)) },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NaiCribsColors.Purple, unfocusedBorderColor = NaiCribsColors.Border, focusedLabelColor = NaiCribsColors.Purple
                    )
                )
                Button(
                    onClick = {
                        val tag = tagInput.trim()
                        if (tag.isNotBlank() && tag !in tagList) {
                            tagList = tagList + tag
                            tagInput = ""
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NaiCribsColors.Purple)
                ) { Text("Add") }
            }
            if (tagList.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tagList.forEach { tag ->
                        AssistChip(
                            onClick = { tagList = tagList - tag },
                            label = { Text(tag, fontSize = 12.sp) },
                            trailingIcon = { Icon(Icons.Default.Close, contentDescription = "Remove tag", modifier = Modifier.size(14.dp)) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = NaiCribsColors.PurpleLight,
                                labelColor = NaiCribsColors.PurpleDark
                            )
                        )
                    }
                }
            }


            NaiCribsTextField(value = contact, onValueChange = { contact = it },
                label = "Contact number *", leadingIcon = Icons.Default.Phone, keyboardType = KeyboardType.Phone)
            NaiCribsTextField(value = email, onValueChange = { email = it },
                label = "Email (optional)", leadingIcon = Icons.Default.Email, keyboardType = KeyboardType.Email)

            Text("Photos", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.TextPrimary)
            if (state.uploadedImageUrls.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.uploadedImageUrls.forEach { url ->
                        Box {
                            AsyncImage(model = url, contentDescription = null, contentScale = ContentScale.Crop,
                                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(10.dp)).border(1.dp, NaiCribsColors.Border, RoundedCornerShape(10.dp)))
                            IconButton(onClick = { viewModel.removeImage(url) },
                                modifier = Modifier.size(22.dp).align(Alignment.TopEnd).background(NaiCribsColors.Error, RoundedCornerShape(11.dp))) {
                                Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(12.dp))
                            }
                        }
                    }
                }
            }
            OutlinedButton(onClick = { imagePicker.launch("image/*") },
                modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NaiCribsColors.Purple)) {
                if (state.isLoading) CircularProgressIndicator(modifier = Modifier.size(18.dp), color = NaiCribsColors.Purple, strokeWidth = 2.dp)
                else { Icon(Icons.Default.AddPhotoAlternate, null, modifier = Modifier.size(20.dp)); Spacer(Modifier.width(8.dp)); Text("Upload Photos") }
            }

            Button(
                onClick = { viewModel.submitListing(name, description, price, location, contact, email, houseType, tagList) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NaiCribsColors.Purple)
            ) {
                if (state.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                else { Icon(Icons.Default.Check, null); Spacer(Modifier.width(8.dp)); Text("Submit Listing", fontSize = 15.sp, fontWeight = FontWeight.SemiBold) }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}