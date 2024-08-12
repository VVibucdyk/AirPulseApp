package com.example.airpulse.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirPulseTopAppBar(onRefresh: () -> Unit) {
    TopAppBar(
        title = { Text(text = "AirPulse") },
        actions = {
            IconButton(onClick = { onRefresh() }) {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Refresh")
            }
        }
    )
}