package com.distlog.reconciliation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.distlog.reconciliation.ui.theme.*

@Composable
fun InfoScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("DISTLOG OVERVIEW", style = MaterialTheme.typography.headlineMedium, color = NeonBlue, fontWeight = FontWeight.ExtraBold)
        Text("Distributed Log Reconciliation & Anomaly Detection", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        
        Spacer(Modifier.height(24.dp))
        
        InfoSection("Problem Statement", "Modern microservices generate millions of logs that are often out of order, duplicated, or missing entirely due to network latency. Traditional ELK stacks don't always reconcile these across services in real-time.")
        
        InfoSection("Our Solution", "DISTLOG uses a normalization engine to synchronize timestamps from disparate formats and a reconciliation algorithm to detect gaps and redundancies, presenting a unified global event timeline.")
        
        InfoSection("Architecture", "Built on MVVM with Kotlin Coroutines for asynchronous processing. Features a modular Repository layer designed to integrate with Kafka or ELK Stack for production environments.")
        
        InfoSection("Future Scope", "Integration with generative AI for root cause prediction, real-time Kafka streaming support, and cloud-native auto-scaling for ingestion pipelines.")

        Spacer(Modifier.height(40.dp))
        Text("HACKATHON PRESENTATION MODE", color = NeonPurple, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun InfoSection(title: String, body: String) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(title.uppercase(), color = NeonPink, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall, letterSpacing = 1.sp)
        Spacer(Modifier.height(8.dp))
        Text(body, color = TextPrimary, style = MaterialTheme.typography.bodyLarge, lineHeight = 24.sp)
        Spacer(Modifier.height(16.dp))
        Divider(color = CardBackground)
    }
}
