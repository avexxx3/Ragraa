package com.avex.ragraa.data

import com.avex.ragraa.sharedPreferences
import com.twocaptcha.TwoCaptcha
import com.twocaptcha.captcha.ReCaptcha
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


object CaptchaSolver {
    val solver = TwoCaptcha("")
    var key = ""

    init {
        setCKey(Datasource.captchaKey)
    }

    fun setCKey(key: String) {
        solver.setApiKey(key)
        this.key = key
        sharedPreferences.edit().putString("captchaKey", key).apply()
    }

    suspend fun solveCaptcha(): Result<String> {
        if (key.isEmpty())
            return Result.failure(Exception("Key is empty"))

        return withContext(Dispatchers.IO) {
            val captcha = ReCaptcha().apply {
                setSiteKey("6LeMxrMZAAAAAJEK1UwUc0C-ScFUyJy07f8YN70S")
                setUrl("https://flexstudent.nu.edu.pk/Login")
                setInvisible(true)
                setAction("verify")
                setProxy("HTTPS", "login:password@IP_address:PORT")
            }

            try {
                // This blocking call now happens off the Main thread
                solver.solve(captcha)
                Result.success(captcha.getCode())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}