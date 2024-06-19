package com.avex.ragraa.ui.pastpapers

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import java.util.concurrent.TimeUnit

class PastPaperViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PastPaperUiState())
    val uiState: StateFlow<PastPaperUiState> = _uiState.asStateFlow()

    init {
        fetchPastPapers()
    }

    private fun fetchPastPapers() {
        val request = Request.Builder()
            .url("https://api.github.com/repos/saleha-muzammil/Academic-Time-Machine/contents")
            .build()

        val client = OkHttpClient().newBuilder().connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES).readTimeout(5, TimeUnit.MINUTES).build()

        Log.d("Dev", "Pov")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("Dev", response.body?.string().toString())
                parseRequest(response)
            }
        })
    }

    private fun parseRequest(response: Response) {

    }
}