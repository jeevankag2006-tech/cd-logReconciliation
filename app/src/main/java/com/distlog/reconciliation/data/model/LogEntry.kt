package com.distlog.reconciliation.data.model

import com.google.gson.annotations.SerializedName

data class LogEntry(
    val id: String,
    val service: String,
    val timestamp: String,
    val event: String,
    val severity: String = "INFO",
    val transactionId: String? = null,
    @SerializedName("isDuplicate") var isDuplicate: Boolean = false,
    @SerializedName("isMissing") var isMissing: Boolean = false,
    var normalizedTimestamp: Long = 0L
) {
    fun normalize() {
        normalizedTimestamp = try {
            when {
                // ISO 8601
                timestamp.contains("T") && timestamp.contains("Z") -> {
                    java.time.Instant.parse(timestamp).toEpochMilli()
                }
                // UNIX Timestamp
                timestamp.all { it.isDigit() } -> {
                    val t = timestamp.toLong()
                    if (t < 1000000000000L) t * 1000 else t
                }
                // HH:mm:ss
                timestamp.contains(":") && !timestamp.contains("-") -> {
                    val sdf = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
                    val date = sdf.parse(timestamp)
                    date?.time ?: System.currentTimeMillis()
                }
                else -> System.currentTimeMillis()
            }
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
}
