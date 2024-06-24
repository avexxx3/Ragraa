package com.avex.ragraa.ui.pastpapers

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.avex.ragraa.AppCompatActivity
import com.avex.ragraa.context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PastPaperFileViewModel(private val file: PastPaperFile, private val directory: String) :
    ViewModel() {
    private val _uiState = MutableStateFlow(file)
    val uiState: StateFlow<PastPaperFile> = _uiState.asStateFlow()

    private var isDownloading: Boolean = false
    private var isDownloaded: Boolean = false

    init {
        updateUI()
    }

    private fun updateUI() {
        _uiState.update {
            it.copy(
                isDownloading = isDownloading,
                isDownloaded = isDownloaded
            )
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    fun downloadFile() {
        isDownloading = true
        updateUI()

        val downloadManager =
            (AppCompatActivity as Activity).getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(file.url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(file.name).setDestinationInExternalFilesDir(context, directory, file.name)

        val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context, intent: Intent) {
                isDownloaded = true
                updateUI()
            }
        }

        context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        downloadManager.enqueue(request)
    }


    fun openFile() {
        val browserIntent = Intent(
            Intent.ACTION_VIEW, Uri.parse(file.url)
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, browserIntent, null)
    }
}