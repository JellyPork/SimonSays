package com.changrojasalex1168165.simonsays.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.changrojasalex1168165.simonsays.database.GameViewModel
import com.changrojasalex1168165.simonsays.dto.ThemeManager

@Composable
fun SettingsComponent() {
    val isDarkThemeEnabled = ThemeManager.isDarkThemeEnabled
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Theme Settings", style = MaterialTheme.typography.displayLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Dark Mode",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isDarkThemeEnabled,
                onCheckedChange = { ThemeManager.isDarkThemeEnabled = !isDarkThemeEnabled }
            )
        }
    }
}