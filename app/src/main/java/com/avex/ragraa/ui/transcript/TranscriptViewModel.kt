package com.avex.ragraa.ui.transcript

import androidx.lifecycle.ViewModel
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.data.Transcript
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TranscriptViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TranscriptUiState())
    val uiState: StateFlow<TranscriptUiState> = _uiState.asStateFlow()

    init {
        Datasource.updateTranscriptUI = { updateUI() }
        updateUI()
    }

    private fun updateUI() {
        _uiState.update { TranscriptUiState(Datasource.transcriptDatabase) }
    }
}

data class TranscriptUiState(
    val transcript: Transcript = Transcript(0f, listOf())
)