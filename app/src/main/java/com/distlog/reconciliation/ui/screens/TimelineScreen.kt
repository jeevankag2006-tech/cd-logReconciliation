package com.distlog.reconciliation.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.distlog.reconciliation.data.model.LogEntry
import com.distlog.reconciliation.ui.theme.*
import com.distlog.reconciliation.viewmodel.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TimelineScreen(viewModel: DashboardViewModel) {
    val logs by viewModel.logs.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") }
    var selectedLevel by remember { mutableStateOf("ALL") }

    val filteredLogs = logs.filter { log ->
        val matchesSearch = log.event.contains(searchQuery, true) || 
                          log.service.contains(searchQuery, true) || 
                          log.transactionId?.contains(searchQuery, true) == true
        val matchesFilter = when(selectedFilter) {
            "DUPLICATES" -> log.isDuplicate
            "GAPS" -> log.isMissing
            else -> true
        }
        val matchesLevel = if (selectedLevel == "ALL") true else log.severity == selectedLevel
        
        matchesSearch && matchesFilter && matchesLevel
    }

    Column(modifier = Modifier.fillMaxSize().background(DarkBackground).padding(16.dp)) {
        Text("Reconstructed Timeline", style = MaterialTheme.typography.headlineMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
        Text("Unified global sequence from Services A, B, and C", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        
        Spacer(Modifier.height(16.dp))
        FilterSection(selectedFilter, onFilterChange = { selectedFilter = it })
        
        Spacer(Modifier.height(8.dp))
        LevelFilterRow(selectedLevel, onLevelChange = { selectedLevel = it })

        Spacer(Modifier.height(16.dp))
        SearchBar(searchQuery) { searchQuery = it }
        
        Spacer(Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(filteredLogs, key = { it.id + it.normalizedTimestamp + it.event }) { log ->
                LogTimelineItem(log)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSection(selected: String, onFilterChange: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("ALL", "DUPLICATES", "GAPS").forEach { filter ->
            FilterChip(
                selected = selected == filter,
                onClick = { onFilterChange(filter) },
                label = { Text(filter) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = NeonBlue.copy(0.2f),
                    selectedLabelColor = NeonBlue,
                    labelColor = TextSecondary
                )
            )
        }
    }
}

@Composable
fun LevelFilterRow(selected: String, onLevelChange: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        listOf("ALL", "INFO", "WARN", "ERROR", "DEBUG").forEach { level ->
            Text(
                text = level,
                modifier = Modifier
                    .clickable { onLevelChange(level) }
                    .padding(vertical = 4.dp),
                color = if (selected == level) NeonPink else TextSecondary,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (selected == level) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search event or service ID...", color = TextSecondary) },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = { Icon(Icons.Default.Search, null, tint = NeonBlue) },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = NeonBlue,
            unfocusedBorderColor = CardBackground,
            containerColor = CardBackground.copy(0.5f)
        )
    )
}

@Composable
fun LogTimelineItem(log: LogEntry) {
    val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date(log.normalizedTimestamp))
    val accentColor = when {
        log.isDuplicate -> WarningOrange
        log.severity == "ERROR" -> ErrorRed
        log.isMissing -> NeonPink
        else -> NeonBlue
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .background(CardBackground, RoundedCornerShape(12.dp))
        .border(1.dp, if (log.isDuplicate) ErrorRed.copy(0.5f) else accentColor.copy(0.2f), RoundedCornerShape(12.dp))
        .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.width(70.dp)) {
                Text(time, color = TextPrimary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                ServiceBadge(log.service)
            }
            
            Spacer(Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(log.event, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                log.transactionId?.let {
                    Text("TXN: $it", color = TextSecondary, fontSize = 11.sp)
                }
                if (log.isDuplicate) {
                    Text("REDUNDANCY DETECTED", color = WarningOrange, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                }
                if (log.isMissing) {
                    Text("SEQUENCE INTERRUPTION", color = NeonPink, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
            
            LogLevelBadge(log.severity)
        }
    }
}

@Composable
fun ServiceBadge(service: String) {
    val color = when(service) {
        "A" -> NeonBlue
        "B" -> NeonPurple
        else -> NeonPink
    }
    Surface(color = color.copy(0.1f), shape = RoundedCornerShape(4.dp), border = androidx.compose.foundation.BorderStroke(0.5.dp, color.copy(0.5f))) {
        Text(service, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = color)
    }
}

@Composable
fun LogLevelBadge(level: String) {
    val color = when(level) {
        "ERROR" -> ErrorRed
        "WARN" -> WarningOrange
        "DEBUG" -> NeonPurple
        else -> SuccessGreen
    }
    Text(level, color = color, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
}
