package com.avex.ragraa.ui.updater

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

object UpdateChecker {
    val currentVersion = 1.2
    var newVersion = 0f
    var updateURL = ""

    fun fetchInfo() {
        val client = OkHttpClient().newBuilder().connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES).readTimeout(5, TimeUnit.MINUTES).build()

        val request = Request.Builder()
            .url("https://api.github.com/repos/avexxx3/Ragraa/releases/latest")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                Log.d("Dev", "Failed to fetch update")
            }

            override fun onResponse(call: Call, response: Response) {
                val response = response.body?.string().toString()
                val version = response.substring(response.indexOf("tag_name") + 12)
                newVersion = version.substring(0, version.indexOf("\"")).toFloat()

                updateURL = response.substring(response.indexOf("browser_download_url") + 24)
                updateURL = updateURL.substring(0, updateURL.indexOf("\""))

                compareRelease()
            }
        }
        )
    }

    fun compareRelease() {
        if (currentVersion >= newVersion)
            return;
        Log.d("Dev", "New version found: $newVersion")
    }
}