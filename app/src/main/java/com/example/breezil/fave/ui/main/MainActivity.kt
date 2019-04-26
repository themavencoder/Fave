package com.example.breezil.fave.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.databinding.DataBindingUtil
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.Toolbar

import com.example.breezil.fave.R
import com.example.breezil.fave.databinding.ActivityMainBinding
import com.example.breezil.fave.ui.explore.SearchActivity
import com.example.breezil.fave.ui.adapter.PagerAdapter
import com.example.breezil.fave.ui.preference.SettingsActivity
import com.example.breezil.fave.utils.helpers.BottomNavigationHelper

import javax.inject.Inject

import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.HasSupportFragmentInjector
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

class MainActivity : DaggerAppCompatActivity(), HasSupportFragmentInjector {


    lateinit var binding: ActivityMainBinding
    private var sharedPreferences: SharedPreferences? = null
    private var themeMode: Boolean = false


    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var pagerAdapter: PagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        themeMode = sharedPreferences!!.getBoolean(getString(R.string.pref_theme_key), false)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        if (themeMode) {
            setTheme(R.style.DarkNoActionTheme)
        } else {
            setTheme(R.style.AppNoActionTheme)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)




        setupAdapter()
        setupBottomNavigation()
        setSupportActionBar(binding.mainToolbar as Toolbar)
        supportActionBar!!.title = getString(R.string.app_name)

        val logging = HttpLoggingInterceptor { message -> Timber.tag(getString(R.string.okhttp)).d(message) }
        logging.redactHeader(getString(R.string.authorization))
        logging.redactHeader(getString(R.string.cookie))
    }

    private fun setupBottomNavigation() {

        BottomNavigationHelper.disableShiftMode(binding.bottomNavViewBar)


        val menu = binding.bottomNavViewBar.menu
        val menuItem = menu.getItem(0)
        menuItem.isChecked = true


        /*
         * here sets the navigations to its corresponding activities
         */
        binding.bottomNavViewBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.main -> {
                }
                R.id.search -> {
                    startActivity(Intent(this@MainActivity, SearchActivity::class.java))
                    finish()
                }
                R.id.settings -> {
                    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                    finish()
                }
            }


            false
        }

    }


    internal fun setupAdapter() {
        pagerAdapter = PagerAdapter(supportFragmentManager, this)
        binding.container.adapter = pagerAdapter
        binding.tabs.setupWithViewPager(binding.container)
        binding.container.setCurrentItem(1, false)

    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return dispatchingAndroidInjector
    }
}
