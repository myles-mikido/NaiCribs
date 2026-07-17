package com.example.naicribs.screens.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

private data class FaqItem(val question: String, val answer: String)

@Composable
fun HelpScreen(navController: NavController) {

    val customerFaqs = listOf(
        FaqItem(
            "How do I search for a house?",
            "Use the search bar on the Browse screen. Type a location, area name, or house type. You can also use the filter chips to narrow down by type (Bedsitter, 1 Bedroom, etc.)",
        ),
        FaqItem(
            "How do I save a listing?",
            "On any listing card, tap the bookmark/heart icon to save it. Access your saved listings from the 'Saved' section on your dashboard.",
        ),
        FaqItem(
            "How do I contact an agent?",
            "On any listing, tap 'Call' to dial the agent directly, or 'Email' to send them a message. You can also tap 'Map' to view the exact location.",
        ),
        FaqItem(
            "How do I view the location of a house?",
            "Tap the 'Map' button on any listing card. This opens a full map showing the exact location pinned by the agent.",
        ),
        FaqItem(
            "Is NaiCribs free to use?",
            "Yes! Browsing and contacting agents is completely free for customers.",
        ),
    )

    val agentFaqs = listOf(
        FaqItem(
            "How do I add a new listing?",
            "From your Agent Dashboard, tap 'Add New Listing'. Fill in the apartment details, upload photos, pin the location on the map, and add area tags.",
        ),
        FaqItem(
            "What are area tags?",
            "Tags are searchable keywords for your listing e.g. 'Westlands', 'Near CBD', 'Gated Community'. They help customers find your listing when they search for those terms.",
        ),
        FaqItem(
            "How do I edit or delete a listing?",
            "Go to 'My Listings' from your dashboard. Each listing card has Edit and Delete buttons.",
        ),
        FaqItem(
            "How do I pin a location on the map?",
            "When adding a listing, tap the map icon next to the location field. Tap anywhere on the map to pin the exact location, then tap 'Confirm location'.",
        ),
        FaqItem(
            "How many photos can I upload?",
            "You can upload multiple photos per listing. We recommend at least 3 clear photos to attract more customers.",
        ),
    )

    Scaffold(
        topBar = { NaiCribsTopBar(title = "Help & FAQ", onBackClick = { navController.popBackStack() }) },
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
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text("Need more help?", style = MaterialTheme.typography.titleSmall, color = Color.White)
                            Spacer(Modifier.height(4.dp))
                            Text("📧 support@naicribs.co.ke", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.9f))
                            Text("📞 +254 700 000 000", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.9f))
                        }
                    }

                    FaqSection(title = "For Customers 🏠", faqs = customerFaqs)
                    FaqSection(title = "For Agents 🗝️", faqs = agentFaqs)

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun FaqSection(title: String, faqs: List<FaqItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
        faqs.forEach { FaqCard(it) }
    }
}

@Composable
private fun FaqCard(item: FaqItem) {
    var expanded by remember { mutableStateOf(false) }

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.clickable { expanded = !expanded }) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    item.question,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(tween(200)) + expandVertically(tween(200)),
                exit = fadeOut(tween(150)) + shrinkVertically(tween(150)),
            ) {
                Column {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Text(
                        item.answer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(14.dp),
                    )
                }
            }
        }
    }
}