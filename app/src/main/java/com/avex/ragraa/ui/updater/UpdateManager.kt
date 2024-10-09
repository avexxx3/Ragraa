package com.avex.ragraa.ui.updater

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

object UpdateManager {
    const val CURRENT_VERSION = 1.3
    var newVersion = 0f
    var updateURL = ""

    var updateUI: (Pair<Float, String>) -> Unit = {}

    fun checkUpdate(_updateUI: (Pair<Float, String>) -> Unit) {
        updateUI = _updateUI
        fetchInfo()
    }

    private fun fetchInfo() {
        val client = OkHttpClient().newBuilder().connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES).readTimeout(5, TimeUnit.MINUTES).build()

        val request =
            Request.Builder().url("https://api.github.com/repos/avexxx3/Ragraa/releases/latest")
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                Log.d("Dev", "Failed to fetch update")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string().toString()

                val version = responseString.substring(responseString.indexOf("tag_name") + 12)

                if (version.substring(0, version.indexOf("\"")).isEmpty()) return

                newVersion = version.substring(0, version.indexOf("\"")).toFloat()

                updateURL =
                    responseString.substring(responseString.indexOf("browser_download_url") + 23)
                updateURL = updateURL.substring(0, updateURL.indexOf("\""))

                compareRelease()
            }
        })
    }

    fun compareRelease() {
        if (newVersion > CURRENT_VERSION.toFloat()) updateUI(Pair(newVersion, updateURL))
    }
}