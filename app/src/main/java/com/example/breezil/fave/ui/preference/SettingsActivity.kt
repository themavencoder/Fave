package com.example.breezil.fave.ui.preference

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment

import com.example.breezil.fave.R
import com.example.breezil.fave.databinding.ActivitySettingsBinding
import com.example.breezil.fave.ui.BaseActivity
import com.example.breezil.fave.ui.explore.SearchActivity
import com.example.breezil.fave.ui.main.MainActivity
import com.example.breezil.fave.utils.helpers.BottomNavigationHelper

import javax.inject.Inject

import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector

class SettingsActivity : BaseActivity(), HasSupportFragmentInjector {

    lateinit var binding: ActivitySettingsBinding

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)

        setupBottomNavigation()
        supportActionBar!!.title = "Preference"

        binding.aboutText.setOnClickListener { startActivity(Intent(this, AboutActivity::class.java)) }

    }


    private fun setupBottomNavigation() {

        BottomNavigationHelper.disableShiftMode(binding.bottomNavViewBar)


        val menu = binding.bottomNavViewBar.menu
        val menuItem = menu.getItem(2)
        menuItem.isChecked = true


        /*
         * here sets the navigations to its corresponding activities
         */
        binding.bottomNavViewBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.main -> {
                    startActivity(Intent(this@SettingsActivity, MainActivity::class.java))
                    finish()
                }
                R.id.search -> {
                    startActivity(Intent(this@SettingsActivity, SearchActivity::class.java))
                    finish()
                }
                R.id.settings -> {
                }
            }


            false
        }

    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return dispatchingAndroidInjector
    }
}
