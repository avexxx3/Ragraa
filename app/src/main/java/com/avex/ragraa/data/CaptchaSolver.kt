package com.avex.ragraa.data

import com.avex.ragraa.sharedPreferences
import com.twocaptcha.TwoCaptcha
import com.twocaptcha.captcha.ReCaptcha


object CaptchaSolver {
    val solver = TwoCaptcha("")
    var key = ""

    init {
        setKey(Datasource.captchaKey)
    }

    fun setKey(key: String) {
        solver.setApiKey(key)
        this.key = key
        sharedPreferences.edit().putString("captchaKey", key).apply()
    }

    fun solveCaptcha(): Result<String> {
        if (key.isEmpty())
            return Result.failure(Exception("Key is empty"))

        val captcha = ReCaptcha()
        captcha.setSiteKey("6LeMxrMZAAAAAJEK1UwUc0C-ScFUyJy07f8YN70S")
        captcha.setUrl("https://flexstudent.nu.edu.pk/Login")
        captcha.setInvisible(true)
        captcha.setAction("verify")
        captcha.setProxy("HTTPS", "login:password@IP_address:PORT")


        try {
            solver.solve(captcha)
            return Result.success(captcha.getCode())
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}