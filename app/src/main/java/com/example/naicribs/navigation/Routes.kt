package com.example.naicribs.navigation

object Routes {
    const val Splash          = "splash"
    const val RoleSelection   = "role_selection"

    const val Login    = "login/{role}"
    const val Register = "register/{role}"
    fun login(role: String)    = "login/$role"
    fun register(role: String) = "register/$role"

    const val ForgotPassword = "forgot_password"

    // Agent
    const val AgentDashboard = "agent_dashboard"
    const val AddListing     = "add_listing"
    const val ApartmentForm  = "apartment_form"
    const val MyListings     = "my_listings"
    const val AgentProfile   = "agent_profile"
    const val LocationPicker = "location_picker"
    const val EditListing    = "edit_listing/{apartmentId}"
    fun editListing(id: String) = "edit_listing/$id"

    // Customer
    const val CustomerDashboard = "customer_dashboard"
    const val Browse            = "browse"
    const val SavedListings     = "saved_listings"
    const val MapViewer         = "map_viewer/{name}/{lat}/{lng}/{label}"
    fun mapViewer(name: String, lat: Double, lng: Double, label: String) =
        "map_viewer/$name/$lat/$lng/${label.replace("/", "-")}"
    const val ViewAgentProfile  = "view_agent_profile/{agentId}"
    fun viewAgentProfile(id: String) = "view_agent_profile/$id"


    const val AboutUs = "about_us"
    const val Help    = "help"
}