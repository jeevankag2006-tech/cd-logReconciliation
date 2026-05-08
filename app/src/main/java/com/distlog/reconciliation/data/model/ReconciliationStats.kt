package com.distlog.reconciliation.data.model

data class ReconciliationStats(
    val totalLogs: Int = 0,
    val duplicatesDetected: Int = 0,
    val missingLogs: Int = 0,
    val errorLogs: Int = 0,
    val warnLogs: Int = 0,
    val healthyServices: Int = 3,
    val processingStatus: String = "Stable",
    val confidenceScore: Float = 1.0f,
    val riskScore: Int = 0,
    val aiInsights: List<AiInsight> = emptyList(),
    val isLiveStreaming: Boolean = false
)

data class AiInsight(
    val title: String,
    val description: String,
    val severity: String, // Critical, Warning, Info
    val type: String
)
