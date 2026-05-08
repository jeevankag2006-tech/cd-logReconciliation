package com.distlog.reconciliation.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.distlog.reconciliation.data.repository.LogRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LogRepository(application)
    
    val logs = repository.logs
    val stats = repository.stats

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing = _isProcessing.asStateFlow()

    private val _isAiRunning = MutableStateFlow(false)
    val isAiRunning = _isAiRunning.asStateFlow()

    private val _eventMessage = MutableSharedFlow<String>()
    val eventMessage = _eventMessage.asSharedFlow()

    init {
        viewModelScope.launch {
            repository.loadInitialData()
        }
    }

    fun handleFileUpload(uri: Uri?) {
        if (uri == null) return
        viewModelScope.launch {
            _isProcessing.value = true
            try {
                val context = getApplication<Application>().applicationContext
                val jsonString = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                if (jsonString != null) {
                    repository.parseAndAddLogs(jsonString)
                    _eventMessage.emit("Logs uploaded successfully")
                }
            } catch (e: Exception) {
                _eventMessage.emit("Failed to parse JSON")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun runAiAnalysis() {
        viewModelScope.launch {
            _isAiRunning.value = true
            repository.runAiAnalysis()
            _isAiRunning.value = false
            _eventMessage.emit("AI Analysis Complete")
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _isProcessing.value = true
            kotlinx.coroutines.delay(1000)
            repository.loadInitialData()
            _isProcessing.value = false
            _eventMessage.emit("System Synchronized")
        }
    }

    fun clearAllLogs() {
        repository.clearLogs()
        viewModelScope.launch { _eventMessage.emit("Data Purged") }
    }

    fun exportLogs() {
        viewModelScope.launch {
            _isProcessing.value = true
            val path = repository.exportLogs()
            _isProcessing.value = false
            _eventMessage.emit("Exported to Downloads")
        }
    }
}
