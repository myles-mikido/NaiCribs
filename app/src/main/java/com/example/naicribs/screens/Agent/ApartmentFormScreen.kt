package com.example.naicribs.screens.Agent

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.naicribs.data.AddListingViewModel
import com.example.naicribs.screens.agent.AddListingScreen


@Composable
fun ApartmentFormScreen(navController: NavController, viewModel: AddListingViewModel) {
    AddListingScreen(navController = navController, viewModel = viewModel)
}