package com.avex.ragraa.ui.admitcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avex.ragraa.network.RagraaApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdmitCardUiState(
    val selectedExamType: String = "",
    val expanded: Boolean = false,
    val statusMessage: String = "",
    val isLoading: Boolean = false
)

class AdmitCardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdmitCardUiState())
    val uiState: StateFlow<AdmitCardUiState> = _uiState.asStateFlow()
    
    fun toggleMenu(expanded: Boolean = !_uiState.value.expanded) {
        _uiState.update { it.copy(expanded = expanded) }
    }
    
    fun selectExamType(examType: String) {
        _uiState.update { 
            it.copy(
                selectedExamType = examType,
                expanded = false
            ) 
        }
    }
    
    fun downloadAdmitCard() {
        if (_uiState.value.selectedExamType.isEmpty()) {
            _uiState.update { it.copy(statusMessage = "Error: Please select an exam type") }
            return
        }
        
        _uiState.update { it.copy(isLoading = true, statusMessage = "Downloading admit card...") }
        
        viewModelScope.launch {
            try {
                RagraaApi.downloadAdmitCard(_uiState.value.selectedExamType) { success, message ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            statusMessage = message
                        ) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        statusMessage = "Error: ${e.message ?: "Failed to download admit card"}"
                    ) 
                }
            }
        }
    }
} 