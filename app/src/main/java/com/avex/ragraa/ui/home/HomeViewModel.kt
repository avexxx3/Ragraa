package com.avex.ragraa.ui.home

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.avex.ragraa.context
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.sharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {
    lateinit var navController: NavHostController

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var updated: Boolean = false
    private var showSettings: Boolean = false
    private var showImage: Boolean = true
    private var vibrate: Boolean = false
    private var danger: Boolean = false
    private var startupRefresh: Boolean = false

    init {
        showImage = Datasource.showImage
        Datasource.updateHomeUI = { updateUI() }
        startupRefresh = sharedPreferences.getBoolean("startupRefresh", false)
        updateUI()
    }

    fun refresh() {
        if (Datasource.rollNo.isEmpty() || Datasource.password.isEmpty()) {
            navController.navigate("login")
        } else {
            navController.navigate("web")
        }

    }

    private fun updateUI() {
        if (!updated) {
            for (course in Datasource.marksDatabase) {
                if (course.new) {
                    updated = true
                    break
                }
            }
        }

        if (!danger) {
            for (course in Datasource.attendanceDatabase) {
                if (course.percentage <= 80) {
                    danger = true
                    break
                }
            }
        }

        _uiState.update {
            it.copy(
                rollNo = Datasource.rollNo,
                image = Datasource.bitmap,
                updated = updated,
                showSettings = showSettings,
                showImage = showImage,
                vibrate = vibrate,
                danger = danger,
                date = Datasource.date,
                startupRefresh = startupRefresh
            )
        }
    }

    fun toggleSettings() {
        showSettings = !showSettings
        updateUI()
    }

    fun toggleImage() {
        showImage = !showImage
        sharedPreferences.edit().putBoolean("showImage", showImage).apply()
        updateUI()
    }


    @Suppress("DEPRECATION")
    fun vibrate() {
        val v: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v.vibrate(500)
        }
        vibrate = true
        updateUI()
    }

    fun toggleStartupRefresh() {
        startupRefresh = !startupRefresh
        sharedPreferences.getString("semId", "241").toString()
        sharedPreferences.edit().putBoolean("startupRefresh", startupRefresh).apply()
        updateUI()
    }
}