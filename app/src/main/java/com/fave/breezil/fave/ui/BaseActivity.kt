package com.fave.breezil.fave.ui

import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity

import com.fave.breezil.fave.R


open class BaseActivity : AppCompatActivity() {
    private var sharedPreferences: SharedPreferences? = null

    private var themeMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        themeMode = sharedPreferences!!.getBoolean(getString(R.string.pref_theme_key), false)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        if (themeMode) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.AppTheme)
        }
    }


}
