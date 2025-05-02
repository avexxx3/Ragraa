package com.avex.ragraa.ui.pastpapers

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Downloading
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avex.ragraa.AppCompatActivity
import com.avex.ragraa.context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class PastPaperFileViewModel(private val file: PastPaperFile, private val directory: String) :
    ViewModel() {
    private val _uiState = MutableStateFlow(file)
    val uiState: StateFlow<PastPaperFile> = _uiState.asStateFlow()

    private var isDownloading: Boolean = false
    private var isDownloaded: Boolean = false

    private val parentDirectory = if (directory.length > 12) directory.substring(13) else directory

    private lateinit var selfFile: File

    init {
        fileExists()
        updateUI()
    }

    private fun updateUI() {
        _uiState.update {
            it.copy(
                downloadIcon = if (isDownloaded) Icons.Filled.DownloadDone else if (isDownloading) Icons.Default.Downloading else Icons.Default.Download,
            )
        }
    }

    fun clickDownload() {
        if (isDownloaded) {
            deleteFile()
            return
        }

        if (isDownloading) return

        downloadFile()
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
                isDownloading = false

                Log.d("Dev", "Finished download for: ${file.name}")

                updateUI()
            }
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU)
            context.registerReceiver(
                onComplete,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                Context.RECEIVER_NOT_EXPORTED
            )
        else
            context.registerReceiver(
                onComplete,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            )


        Log.d("Dev", "Starting download for: ${file.name}")

        downloadManager.enqueue(request)

        viewModelScope.launch(Dispatchers.IO) {
            while (!fileExists()) delay(500)
            updateUI()
        }


    }

    private fun fileExists(externalFilesDirs: Array<File> = context.getExternalFilesDirs(null)): Boolean {
        val fileName = file.name

        for (file in externalFilesDirs) {
            if (file.isFile()) {
                if (file.path.substring(67) == "${parentDirectory}/${fileName}") {
                    isDownloaded = true
                    isDownloading = false
                    selfFile = file
                    return true
                }
            } else if (fileExists(file.listFiles()!!)) return true
        }

        return false
    }

    private fun openDownloadedFile() {
        val uri = FileProvider.getUriForFile(
            context, context.applicationContext.packageName + ".provider", selfFile
        )

        val intent = Intent(Intent.ACTION_VIEW).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(uri, getMimeType())

        try {
            startActivity(context, intent, null)
        } catch (e: ActivityNotFoundException) {
            openUrl()
        }
    }

    private fun openUrl() {
        val intent = Intent(
            Intent.ACTION_VIEW, Uri.parse(file.url)
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, intent, null)
    }

    fun openFile() {
        if (isDownloaded) openDownloadedFile()
        else if (!isDownloading) openUrl()
    }

    fun share() {
        if (isDownloaded) shareFile()
        else shareUrl()
    }

    private fun shareUrl() {
        val i = Intent(Intent.ACTION_SEND)
        i.setType("text/plain")
        i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
        i.putExtra(Intent.EXTRA_TEXT, file.url)

        context.startActivity(
            Intent.createChooser(i, "Share URL").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    private fun shareFile() {
        val uri = FileProvider.getUriForFile(
            context, "com.avex.ragraa.provider", selfFile
        )

        val intentShareFile = Intent(Intent.ACTION_SEND)
        intentShareFile.setType("application/pdf")
        intentShareFile.putExtra(Intent.EXTRA_STREAM, uri)
        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Sharing File...")
        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...")

        startActivity(
            context,
            Intent.createChooser(intentShareFile, "Share File")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            null
        )
    }

    private fun deleteFile() {
        if (!isDownloaded) return

        selfFile.delete()
        selfFile.parentFile?.delete()
        isDownloaded = false
        updateUI()
    }

    private fun getMimeType(): String? {
        val ext = file.name.reversed().substring(0, file.name.reversed().indexOf('.')).reversed()
        val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
        return mime
    }
}