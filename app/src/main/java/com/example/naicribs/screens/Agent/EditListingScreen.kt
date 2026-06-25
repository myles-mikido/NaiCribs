package com.example.naicribs.screens.Agent

import androidx.compose.foundation.background
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.naicribs.data.AddListingViewModel
import com.example.naicribs.data.AgentViewModel
import com.example.naicribs.data.ListingRepository
import com.example.naicribs.screens.common.NaiCribsTextField
import com.example.naicribs.screens.common.NaiCribsTopBar
import com.example.naicribs.ui.theme.NaiCribsColors
import kotlinx.coroutines.launch

@Composable
fun EditListingScreen(
    navController: NavController,
    apartmentId: String,
    agentViewModel: AgentViewModel,
    editViewModel: AddListingViewModel
) {
    val scope = rememberCoroutineScope()
    val repo  = remember { ListingRepository() }

    var name         by remember { mutableStateOf("") }
    var description  by remember { mutableStateOf("") }
    var price        by remember { mutableStateOf("") }
    var location     by remember { mutableStateOf("") }
    var contact      by remember { mutableStateOf("") }
    var email        by remember { mutableStateOf("") }
    var houseType    by remember { mutableStateOf("1 Bedroom") }
    var isLoading    by remember { mutableStateOf(true) }
    var isSaving     by remember { mutableStateOf(false) }
    var errorMsg     by remember { mutableStateOf<String?>(null) }
    var typeExpanded by remember { mutableStateOf(false) }

    val houseTypes = listOf("Bedsitter", "1 Bedroom", "2 Bedroom", "3 Bedroom", "4 Bedroom+")


    LaunchedEffect(apartmentId) {
        val apt = agentViewModel.state.value.listings.find { it.id == apartmentId }
        if (apt != null) {
            name        = apt.name
            description = apt.description
            price       = apt.price.toLong().toString()
            location    = apt.location
            contact     = apt.contactNumber
            email       = apt.email
            houseType   = apt.houseType.ifBlank { "1 Bedroom" }
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            NaiCribsTopBar(title = "Edit Listing", onBackClick = { navController.popBackStack() })
        }
    ) { padding ->

        if (isLoading) {
            Box(
                modifier         = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = NaiCribsColors.Purple) }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NaiCribsColors.Background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            errorMsg?.let {
                Surface(shape = RoundedCornerShape(10.dp), color = NaiCribsColors.ErrorLight) {
                    Text(it, modifier = Modifier.padding(12.dp), fontSize = 13.sp, color = NaiCribsColors.Error)
                }
            }

            NaiCribsTextField(value = name, onValueChange = { name = it }, label = "Apartment name", leadingIcon = Icons.Default.Home)

            OutlinedTextField(
                value         = description,
                onValueChange = { description = it },
                label         = { Text("Description", fontSize = 13.sp) },
                leadingIcon   = { Icon(Icons.Default.Notes, contentDescription = null, tint = NaiCribsColors.TextMuted, modifier = Modifier.size(20.dp)) },
                minLines      = 3,
                maxLines      = 5,
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(12.dp),
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = NaiCribsColors.Purple,
                    unfocusedBorderColor = NaiCribsColors.Border,
                    focusedLabelColor    = NaiCribsColors.Purple,
                    cursorColor          = NaiCribsColors.Purple
                )
            )

            NaiCribsTextField(
                value         = price,
                onValueChange = { price = it },
                label         = "Price (KSh)",
                leadingIcon   = Icons.Default.AttachMoney,
                keyboardType  = KeyboardType.Number
            )


            Box {
                OutlinedTextField(
                    value         = houseType,
                    onValueChange = {},
                    readOnly      = true,
                    label         = { Text("House type", fontSize = 13.sp) },
                    trailingIcon  = {
                        IconButton(onClick = { typeExpanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = NaiCribsColors.TextMuted)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = NaiCribsColors.Purple,
                        unfocusedBorderColor = NaiCribsColors.Border,
                        focusedLabelColor    = NaiCribsColors.Purple
                    )
                )
                DropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                    houseTypes.forEach { type ->
                        DropdownMenuItem(
                            text    = { Text(type) },
                            onClick = { houseType = type; typeExpanded = false }
                        )
                    }
                }
            }

            NaiCribsTextField(value = location, onValueChange = { location = it }, label = "Location", leadingIcon = Icons.Default.LocationOn)
            NaiCribsTextField(value = contact,  onValueChange = { contact  = it }, label = "Contact number", leadingIcon = Icons.Default.Phone, keyboardType = KeyboardType.Phone)
            NaiCribsTextField(value = email,    onValueChange = { email    = it }, label = "Email",          leadingIcon = Icons.Default.Email, keyboardType = KeyboardType.Email)

            Button(
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
                            name          = name.trim(),
                            description   = description.trim(),
                            price         = price.toDoubleOrNull() ?: apt.price,
                            location      = location.trim(),
                            contactNumber = contact.trim(),
                            email         = email.trim(),
                            houseType     = houseType
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
                enabled  = !isSaving,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = NaiCribsColors.Teal)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Save Changes", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}