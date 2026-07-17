package com.example.naicribs.screens.agent

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.naicribs.data.AgentViewModel
import com.example.naicribs.models.Apartment
import com.example.naicribs.navigation.Routes

@Composable
fun AgentDashboard(navController: NavController, viewModel: AgentViewModel) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadMyListings() }

    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints {
            val isWideScreen = maxWidth > 600.dp

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            ) {
                // Hero header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.tertiary,
                                )
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 28.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Good day,", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                                Text("Agent Dashboard 👋", style = MaterialTheme.typography.headlineSmall, color = Color.White)
                            }
                            Box(
                                modifier = Modifier.size(48.dp).clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp)) }
                        }
                        Spacer(Modifier.height(20.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            StatChip("My listings", "${state.listings.size}", Modifier.weight(1f))
                            StatChip("Active", "${state.listings.size}", Modifier.weight(1f))
                        }
                    }
                }

                val contentPadding = if (isWideScreen) 32.dp else 20.dp

                // On wide screens, lay actions out as a responsive grid instead of a stack.
                if (isWideScreen) {
                    Column(modifier = Modifier.padding(contentPadding), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        SectionLabel("Quick actions")
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            ActionCard(
                                icon = Icons.Default.Add, title = "Add new listing", description = "Post a vacant property",
                                containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White,
                                iconBg = Color.White.copy(alpha = 0.2f), iconTint = Color.White,
                                onClick = { navController.navigate(Routes.AddListing) }, modifier = Modifier.weight(1f),
                            )
                            ActionCard(
                                icon = Icons.Default.List, title = "My listings", description = "View, edit or remove properties",
                                containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface,
                                iconBg = MaterialTheme.colorScheme.primaryContainer, iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                onClick = { navController.navigate(Routes.MyListings) }, modifier = Modifier.weight(1f),
                            )
                            ActionCard(
                                icon = Icons.Default.Person, title = "My profile", description = "View and update your profile",
                                containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface,
                                iconBg = MaterialTheme.colorScheme.secondaryContainer, iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                                onClick = { navController.navigate(Routes.AgentProfile) }, modifier = Modifier.weight(1f),
                            )
                        }
                        MoreAndRecentSection(navController, state.listings)
                    }
                } else {
                    Column(modifier = Modifier.padding(contentPadding), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        SectionLabel("Quick actions")
                        ActionCard(
                            icon = Icons.Default.Add, title = "Add new listing", description = "Post a vacant property",
                            containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White,
                            iconBg = Color.White.copy(alpha = 0.2f), iconTint = Color.White,
                            onClick = { navController.navigate(Routes.AddListing) },
                        )
                        ActionCard(
                            icon = Icons.Default.List, title = "My listings", description = "View, edit or remove your properties",
                            containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface,
                            iconBg = MaterialTheme.colorScheme.primaryContainer, iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                            onClick = { navController.navigate(Routes.MyListings) },
                        )
                        ActionCard(
                            icon = Icons.Default.Person, title = "My profile", description = "View and update your agent profile",
                            containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface,
                            iconBg = MaterialTheme.colorScheme.secondaryContainer, iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                            onClick = { navController.navigate(Routes.AgentProfile) },
                        )
                        MoreAndRecentSection(navController, state.listings)
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun MoreAndRecentSection(navController: NavController, listings: List<Apartment>) {
    Spacer(Modifier.height(4.dp))
    SectionLabel("More")
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        SmallCard(
            icon = Icons.Default.Info, label = "About us",
            color = MaterialTheme.colorScheme.primaryContainer, iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            onClick = { navController.navigate(Routes.AboutUs) },
            modifier = Modifier.weight(1f),
        )
        SmallCard(
            icon = Icons.Default.HelpOutline, label = "Help",
            color = MaterialTheme.colorScheme.secondaryContainer, iconColor = MaterialTheme.colorScheme.onSecondaryContainer,
            onClick = { navController.navigate(Routes.Help) },
            modifier = Modifier.weight(1f),
        )
    }

    if (listings.isNotEmpty()) {
        Spacer(Modifier.height(4.dp))
        SectionLabel("Most recent listing")
        val latest = listings.first()
        ElevatedCard(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth().animateContentSize(animationSpec = tween(250)),
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Default.Home, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer) }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(latest.name, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                    Text("📍 ${latest.location}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(
                    "KSh ${latest.price.toLong()}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(text, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
private fun StatChip(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(shape = RoundedCornerShape(14.dp), color = Color.White.copy(alpha = 0.16f), modifier = modifier) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(value, style = MaterialTheme.typography.headlineSmall, color = Color.White)
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.75f))
        }
    }
}

@Composable
private fun ActionCard(
    icon: ImageVector, title: String, description: String,
    containerColor: Color, contentColor: Color, onClick: () -> Unit,
    iconBg: Color, iconTint: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick, shape = RoundedCornerShape(18.dp),
        color = containerColor, shadowElevation = 1.dp, modifier = modifier.fillMaxWidth(),
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(46.dp).clip(RoundedCornerShape(12.dp)).background(iconBg),
                contentAlignment = Alignment.Center
            ) { Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp)) }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall, color = contentColor)
                Text(description, style = MaterialTheme.typography.bodySmall, color = contentColor.copy(alpha = 0.7f))
            }
            Icon(Icons.Default.ArrowForwardIos, contentDescription = null,
                tint = contentColor.copy(alpha = 0.5f), modifier = Modifier.size(14.dp))
        }
    }
}

@Composable
private fun SmallCard(
    icon: ImageVector, label: String,
    color: Color, iconColor: Color,
    onClick: () -> Unit, modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick, shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface, shadowElevation = 1.dp, modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier.size(46.dp).clip(RoundedCornerShape(14.dp)).background(color),
                contentAlignment = Alignment.Center
            ) { Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp)) }
            Text(label, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}