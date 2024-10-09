package com.avex.ragraa.ui.pastpapers

import androidx.lifecycle.ViewModel
import com.avex.ragraa.store
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.kotlin.boxFor
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


class PastPaperViewModel(paramDir: PastPaperDirectory = PastPaperDirectory()) : ViewModel() {
    private val _uiState = MutableStateFlow(PastPaperUiState())
    val uiState: StateFlow<PastPaperUiState> = _uiState.asStateFlow()

    private var selfDir = paramDir
    private var viewingDir: PastPaperDirectory? = null
    private var returnFunction: Boolean = false
    private var rateLimited: Boolean = false
    private var showDir: Boolean = false

    private var jsonResponse: String = ""

    init {
        fetchContents()
        updateUI()
    }

    private fun updateUI() {
        _uiState.update {
            PastPaperUiState(
                selfDir = selfDir,
                viewingDir = viewingDir,
                returnFunction = returnFunction,
                rateLimited = rateLimited,
                showDir = showDir
            )
        }
    }

    private fun fetchContents() {
        if (!fetchLocal() || selfDir.sha.isEmpty()) fetchOnline()
    }

    private fun fetchLocal(): Boolean {
        val shaBox = store.boxFor<CachedDirectory>().all
        val mapSHA = shaBox.map { it.sha }
        val index = mapSHA.indexOf(selfDir.sha)
        if (index == -1) return false
        jsonResponse = shaBox[index].json
        parseRequest()
        return true
    }


    private fun fetchOnline() {
        val request = Request.Builder().url(selfDir.url).build()

        val client = OkHttpClient().newBuilder().connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES).readTimeout(5, TimeUnit.MINUTES).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {
                jsonResponse = response.body?.string().toString()

                if (isRateLimited()) return

                removeCache()
                store.boxFor<CachedDirectory>().put(CachedDirectory(selfDir.sha, jsonResponse))

                parseRequest()
            }
        })
    }

    private fun isRateLimited(): Boolean {
        rateLimited = jsonResponse.contains("API rate limit exceeded")
        updateUI()
        return rateLimited
    }

    private fun removeCache() {
        val mapSHA = store.boxFor<CachedDirectory>().all.map { it.sha }

        val index = mapSHA.indexOf(selfDir.sha)

        if (index != -1) {
            store.boxFor<CachedDirectory>().remove(store.boxFor<CachedDirectory>().all[index].id)
        }
    }

    private fun parseRequest() {
        val listType = object : TypeToken<ArrayList<JsonObject>>() {}.type

        val list: ArrayList<JsonObject> = GsonBuilder().create().fromJson(jsonResponse, listType)

        val listOfDir: MutableList<PastPaperDirectory> = mutableListOf()
        val listOfFiles: MutableList<PastPaperFile> = mutableListOf()

        for (item in list) {
            if (item.type == "dir") listOfDir.add(
                PastPaperDirectory(
                    name = item.name!!, url = item.url!!, sha = item.sha!!
                )
            ) else if (item.name!! != "README.md") listOfFiles.add(
                PastPaperFile(
                    name = item.name!!, url = item.downloadUrl!!, sha = item.sha!!
                )
            )
        }

        selfDir = selfDir.copy(
            contents = Contents(listOfDir, listOfFiles), completed = true
        )

        updateUI()
    }

    fun hideDir() {
        showDir = false
        updateUI()
    }

    fun setDirectory(viewDir: PastPaperDirectory?) {
        viewingDir = viewDir
        showDir = true
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
    @SerializedName("_links") var links: Links? = Links()
)

data class Links(
    @SerializedName("self") var self: String? = null,
    @SerializedName("git") var git: String? = null,
    @SerializedName("html") var html: String? = null
)

@Entity
data class CachedDirectory(
    val sha: String, val json: String, @Id var id: Long = 0
)