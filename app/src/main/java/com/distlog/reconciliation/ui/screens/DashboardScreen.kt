package com.distlog.reconciliation.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.distlog.reconciliation.ui.components.*
import com.distlog.reconciliation.ui.theme.*
import com.distlog.reconciliation.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(viewModel: DashboardViewModel, onUploadClick: () -> Unit) {
    val stats by viewModel.stats.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val isAiRunning by viewModel.isAiRunning.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        HeaderSection(stats.processingStatus)
        Spacer(modifier = Modifier.height(24.dp))
        
        StatsGrid(stats)
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            UploadSection(isProcessing, onUploadClick, modifier = Modifier.weight(1f))
            ConfidenceSection(stats.confidenceScore, modifier = Modifier.weight(0.8f))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        AiAnalysisSection(isAiRunning, stats, onRunAi = { viewModel.runAiAnalysis() })
        
        Spacer(modifier = Modifier.height(24.dp))
        ActionButtonsRow(
            onClear = { viewModel.clearAllLogs() },
            onExport = { viewModel.exportLogs() }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        LiveStatusIndicator(stats.isLiveStreaming)
        
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun HeaderSection(status: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Operational Dashboard", style = MaterialTheme.typography.headlineMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
            Text("System State: $status", style = MaterialTheme.typography.bodySmall, color = NeonBlue)
        }
        Icon(Icons.Default.AccountCircle, null, tint = NeonBlue, modifier = Modifier.size(40.dp))
    }
}

@Composable
fun StatsGrid(stats: com.distlog.reconciliation.data.model.ReconciliationStats) {
    val totalLogsAnimate by animateIntAsState(targetValue = stats.totalLogs, animationSpec = tween(1000), label = "")
    
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard("Total Ingested", totalLogsAnimate.toString(), "Stable", Icons.Default.Dataset, NeonBlue, Modifier.weight(1f))
            StatCard("Duplicates", stats.duplicatesDetected.toString(), "Detected", Icons.Default.CopyAll, NeonPurple, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard("Missing / Gaps", stats.missingLogs.toString(), "Flow Loss", Icons.Default.ErrorOutline, NeonPink, Modifier.weight(1f))
            StatCard("System Errors", stats.errorLogs.toString(), if (stats.errorLogs > 50) "Critical" else "Warning", Icons.Default.BugReport, ErrorRed, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard("Security Risk", "${stats.riskScore}%", if (stats.riskScore > 30) "High" else "Minimal", Icons.Default.Security, SuccessGreen, Modifier.weight(1f))
            StatCard("Healthy Nodes", "${stats.healthyServices}/3", "Online", Icons.Default.CloudQueue, NeonBlue, Modifier.weight(1f))
        }
    }
}

@Composable
fun UploadSection(isProcessing: Boolean, onUploadClick: () -> Unit, modifier: Modifier = Modifier) {
    GlassCard(modifier = modifier.height(180.dp), glowColor = NeonBlue) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
            if (isProcessing) {
                CircularProgressIndicator(color = NeonBlue)
            } else {
                Icon(Icons.Default.CloudUpload, null, tint = NeonBlue, modifier = Modifier.size(40.dp))
                Spacer(Modifier.height(8.dp))
                Text("Upload Logs", color = TextPrimary, fontWeight = FontWeight.Medium)
                Text("JSON / Multiple", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                Spacer(Modifier.height(16.dp))
                NeonButton("SELECT FILES", onUploadClick, Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun ConfidenceSection(score: Float, modifier: Modifier = Modifier) {
    val animatedProgress by animateFloatAsState(targetValue = score, animationSpec = tween(1500), label = "")
    val color = if (score > 0.8f) SuccessGreen else if (score > 0.5f) WarningOrange else ErrorRed

    GlassCard(modifier = modifier.height(180.dp), glowColor = color) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(80.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(color.copy(0.1f), 0f, 360f, false, style = Stroke(6.dp.toPx(), cap = StrokeCap.Round))
                    drawArc(color, -90f, 360f * animatedProgress, false, style = Stroke(6.dp.toPx(), cap = StrokeCap.Round))
                }
                Text("${(score * 100).toInt()}%", color = color, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text("Confidence", color = TextPrimary, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun AiAnalysisSection(isRunning: Boolean, stats: com.distlog.reconciliation.data.model.ReconciliationStats, onRunAi: () -> Unit) {
    GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonPurple) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoGraph, null, tint = NeonPurple, modifier = Modifier.size(32.dp))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("AI Anomaly Engine", color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text("Deep sequence analysis active", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                }
                Spacer(Modifier.weight(1f))
                if (isRunning) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = NeonPurple, strokeWidth = 2.dp)
                } else {
                    NeonButton("RUN AI SCAN", onRunAi, color = NeonPurple)
                }
            }
            
            if (stats.aiInsights.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                stats.aiInsights.forEach { insight ->
                    AiInsightItem(insight)
                }
            }
        }
    }
}

@Composable
fun AiInsightItem(insight: com.distlog.reconciliation.data.model.AiInsight) {
    val color = when(insight.severity) {
        "Critical" -> ErrorRed
        "Warning" -> WarningOrange
        else -> NeonBlue
    }
    Row(modifier = Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(6.dp).background(color, androidx.compose.foundation.shape.CircleShape))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(insight.title, color = color, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            Text(insight.description, color = TextPrimary, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun ActionButtonsRow(onClear: () -> Unit, onExport: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        NeonButton("PURGE DATA", onClear, Modifier.weight(1f), color = ErrorRed)
        NeonButton("EXPORT JSON", onExport, Modifier.weight(1f), color = NeonBlue)
    }
}

@Composable
fun LiveStatusIndicator(isLive: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse), label = ""
    )

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
        Box(Modifier.size(8.dp).background(if (isLive) SuccessGreen else NeonPurple.copy(alpha), androidx.compose.foundation.shape.CircleShape))
        Spacer(Modifier.width(8.dp))
        Text(
            if (isLive) "LIVE STREAM ACTIVE" else "KAFKA PIPELINE READY",
            color = if (isLive) SuccessGreen else TextSecondary,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}
