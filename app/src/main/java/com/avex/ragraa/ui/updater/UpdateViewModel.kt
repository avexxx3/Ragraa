package com.avex.ragraa.ui.updater

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.avex.ragraa.context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class UpdateViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UpdateUIState())
    val uiState: StateFlow<UpdateUIState> = _uiState.asStateFlow()

    var newVersion = 0f
    var updateURL = ""
    var showPrompt = false

    fun updateUI() {
        _uiState.update {
            it.copy(
                newVersion = newVersion,
                updateURL = updateURL,
                showPrompt = showPrompt
            )
        }
    }

    fun closePrompt() {
        showPrompt = false
        updateUI()
    }

    fun collectData(source: Pair<Float, String>) {
        newVersion = source.first
        updateURL = source.second
        showPrompt = true
        updateUI()
    }

    fun launchURL() {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(updateURL)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, browserIntent, null)
    }
}

data class UpdateUIState(
    val newVersion: Float = 0f,
    val updateURL: String = "",
    val showPrompt: Boolean = false
)