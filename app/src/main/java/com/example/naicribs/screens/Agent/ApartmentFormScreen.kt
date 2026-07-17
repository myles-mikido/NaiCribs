package com.example.naicribs.screens.Agent

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.naicribs.data.AddListingViewModel
import com.example.naicribs.screens.agent.AddListingScreen

/**
 * Thin wrapper kept for navigation-graph compatibility.
 * The redesigned MD3 form itself lives in [AddListingScreen].
 */
@Composable
fun ApartmentFormScreen(navController: NavController, viewModel: AddListingViewModel) {
    val showPass = false
    AddListingScreen(navController = navController, viewModel = viewModel, showPass = showPass)
}