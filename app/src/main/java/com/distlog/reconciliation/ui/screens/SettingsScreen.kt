package com.distlog.reconciliation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.distlog.reconciliation.data.SettingsManager
import com.distlog.reconciliation.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }
    val scope = rememberCoroutineScope()
    
    val darkMode by settingsManager.isDarkMode.collectAsState(initial = true)
    val animations by settingsManager.isAnimationsEnabled.collectAsState(initial = true)
    var connectionStatus by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize().background(DarkBackground).padding(16.dp)) {
        Text("System Settings", style = MaterialTheme.typography.headlineMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(32.dp))
        
        SettingToggle("Dark Mode", "Use cyberpunk theme", darkMode) { 
            scope.launch { settingsManager.setDarkMode(it) }
        }
        SettingToggle("UI Animations", "Enable smooth transitions", animations) { 
            scope.launch { settingsManager.setAnimationsEnabled(it) }
        }
        SettingToggle("Live Sync", "Simulate Kafka streaming", connectionStatus) { connectionStatus = it }
        
        Spacer(Modifier.height(24.dp))
        Divider(color = CardBackground)
        Spacer(Modifier.height(24.dp))
        
        Text("Developer Options", color = NeonBlue, style = MaterialTheme.typography.labelSmall)
        Spacer(Modifier.height(16.dp))
        
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple.copy(0.1f), contentColor = NeonPurple)
        ) {
            Text("RESET SYSTEM CACHE")
        }
        
        Spacer(Modifier.weight(1f))
        
        Text("Version 2.0.1-Hackathon-Stable", color = TextSecondary, style = MaterialTheme.typography.labelSmall, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun SettingToggle(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
            Text(subtitle, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = NeonBlue,
                checkedTrackColor = NeonBlue.copy(0.3f),
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = CardBackground
            )
        )
    }
}
