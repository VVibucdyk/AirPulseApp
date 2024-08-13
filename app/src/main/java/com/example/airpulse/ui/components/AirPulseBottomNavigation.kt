package com.example.airpulse.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AirPulseBottomNavigation(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    NavigationBar {
        val items = listOf("Home")
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    when (item) {
                        "Home" -> Icon(imageVector = Icons.Filled.Home, contentDescription = "Home")
                    }
                },
                label = { Text(item) },
                selected = item == selectedTab,
                onClick = { onTabSelected(item) }
            )
        }
    }
}