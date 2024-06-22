package com.avex.ragraa.ui.pastpapers

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    private val courses: MutableList<PastPaperDirectory> = mutableListOf()

    init {
        fetchPastPapers()
    }

    private fun updateUI() {
        _uiState.update {
            PastPaperUiState(
                courses = courses,
                completed = true
            )
        }
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
                parseRequest(response.body?.string().toString())
            }
        })
    }

    private fun parseRequest(response: String) {
        val gson = Gson()

        val listType = object : TypeToken<ArrayList<JsonObject>>() {}.type

        val list: ArrayList<JsonObject> = GsonBuilder().create().fromJson(response, listType)

        for (item in list) {
            courses.add(
                PastPaperDirectory(
                    name = item.name!!,
                    url = item.url!!,
                    type = Type.Directory
                )
            )
        }

        updateUI()
    }
}

data class JsonObject(
    @SerializedName("name") var name: String? = null,
    @SerializedName("path") var path: String? = null,
    @SerializedName("sha") var sha: String? = null,
    @SerializedName("size") var size: Int? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("html_url") var htmlUrl: String? = null,
    @SerializedName("git_url") var gitUrl: String? = null,
    @SerializedName("download_url") var downloadUrl: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("_links") var Links: Links? = Links()
)

data class Links(
    @SerializedName("self") var self: String? = null,
    @SerializedName("git") var git: String? = null,
    @SerializedName("html") var html: String? = null
)