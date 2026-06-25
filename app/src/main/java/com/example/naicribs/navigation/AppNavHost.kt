package com.example.naicribs.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.naicribs.data.*
import com.example.naicribs.screens.Agent.AgentProfileScreen
import com.example.naicribs.screens.Agent.ApartmentFormScreen
import com.example.naicribs.screens.Agent.EditListingScreen
import com.example.naicribs.screens.Agent.MyListingsScreen
import com.example.naicribs.screens.Customer.BrowseScreen
import com.example.naicribs.screens.Customer.ViewAgentProfileScreen
import com.example.naicribs.screens.LocationPickerScreen
import com.example.naicribs.screens.agent.AddListingScreen
import com.example.naicribs.screens.agent.AgentDashboard
import com.example.naicribs.screens.auth.*
import com.example.naicribs.screens.common.*
import com.example.naicribs.screens.customer.*
import com.example.naicribs.screens.splash.SplashScreen

@Composable
fun AppNavHost() {
    val navController      = rememberNavController()
    val authViewModel:     AuthViewModel     = viewModel()
    val agentViewModel:    AgentViewModel    = viewModel()
    val customerViewModel: CustomerViewModel = viewModel()

    NavHost(
        navController    = navController,
        startDestination = Routes.Splash
    ) {


        composable(Routes.Splash) {
            SplashScreen(navController = navController)
        }

        composable(Routes.RoleSelection) {
            RoleSelectionScreen(navController = navController)
        }

        composable(
            route     = Routes.Login,
            arguments = listOf(navArgument("role") { type = NavType.StringType; defaultValue = "customer" })
        ) { backStack ->
            val role = backStack.arguments?.getString("role") ?: "customer"
            LoginScreen(navController = navController, viewModel = authViewModel, role = role)
        }

        composable(
            route     = Routes.Register,
            arguments = listOf(navArgument("role") { type = NavType.StringType; defaultValue = "customer" })
        ) { backStack ->
            val role = backStack.arguments?.getString("role") ?: "customer"
            RegisterScreen(navController = navController, viewModel = authViewModel, role = role)
        }

        composable(Routes.ForgotPassword) {
            ForgotPasswordScreen(navController = navController, viewModel = authViewModel)
        }

        composable(Routes.AgentDashboard) {
            AgentDashboard(navController = navController, viewModel = agentViewModel)
        }

        composable(Routes.AddListing) {
            val addViewModel: AddListingViewModel = viewModel()
            AddListingScreen(navController = navController, viewModel = addViewModel)
        }

        composable(Routes.ApartmentForm) {
            val addViewModel: AddListingViewModel = viewModel()
            ApartmentFormScreen(navController = navController, viewModel = addViewModel)
        }

        composable(Routes.MyListings) {
            MyListingsScreen(navController = navController, viewModel = agentViewModel)
        }

        composable(Routes.AgentProfile) {
            AgentProfileScreen(navController = navController, viewModel = authViewModel)
        }

        composable(Routes.LocationPicker) {
            LocationPickerScreen(navController = navController)
        }

        composable(
            route     = Routes.EditListing,
            arguments = listOf(navArgument("apartmentId") { type = NavType.StringType })
        ) { backStack ->
            val apartmentId = backStack.arguments?.getString("apartmentId") ?: ""
            val editViewModel: AddListingViewModel = viewModel()
            EditListingScreen(
                navController  = navController,
                apartmentId    = apartmentId,
                agentViewModel = agentViewModel,
                editViewModel  = editViewModel
            )
        }

        composable(Routes.CustomerDashboard) {
            CustomerDashboard(navController = navController, viewModel = customerViewModel)
        }

        composable(Routes.Browse) {
            BrowseScreen(navController = navController, viewModel = customerViewModel)
        }

        composable(Routes.SavedListings) {
            SavedListingsScreen(navController = navController, viewModel = customerViewModel)
        }

        composable(
            route     = Routes.MapViewer,
            arguments = listOf(
                navArgument("name")  { type = NavType.StringType },
                navArgument("lat")   { type = NavType.StringType },
                navArgument("lng")   { type = NavType.StringType },
                navArgument("label") { type = NavType.StringType }
            )
        ) { backStack ->
            val name  = backStack.arguments?.getString("name")  ?: ""
            val lat   = backStack.arguments?.getString("lat")?.toDoubleOrNull()  ?: 0.0
            val lng   = backStack.arguments?.getString("lng")?.toDoubleOrNull()  ?: 0.0
            val label = backStack.arguments?.getString("label") ?: ""
            MapViewerScreen(
                navController = navController,
                apartmentName = name, latitude = lat, longitude = lng, locationLabel = label
            )
        }

        composable(
            route     = Routes.ViewAgentProfile,
            arguments = listOf(navArgument("agentId") { type = NavType.StringType })
        ) { backStack ->
            val agentId = backStack.arguments?.getString("agentId") ?: ""
            ViewAgentProfileScreen(navController = navController, agentId = agentId)
        }

        composable(Routes.AboutUs) {
            AboutUsScreen(navController = navController)
        }

        composable(Routes.Help) {
            HelpScreen(navController = navController)
        }
    }
}