package com.fave.breezil.fave.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.fave.breezil.fave.R
import com.fave.breezil.fave.ui.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {
    private var sharedPreferences: SharedPreferences? = null
    private var themeMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        themeMode = sharedPreferences!!.getBoolean(getString(R.string.pref_theme_key), false)

        if (themeMode) {
            setTheme(R.style.DarkNoActionTheme)
        } else {
            setTheme(R.style.AppNoActionTheme)
        }
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }

    companion object {

        private const val SPLASH_TIME_OUT = 3000
    }
}
