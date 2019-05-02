package com.fave.breezil.fave.ui.preference

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v14.preference.MultiSelectListPreference
import android.support.v14.preference.SwitchPreference
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewCompat
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceGroup
import android.support.v7.preference.PreferenceManager
import android.view.View
import android.widget.ListView

import com.fave.breezil.fave.R

import java.util.ArrayList
import java.util.Objects

class PrefFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var mSourcePref: MultiSelectListPreference? = null


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        addPreferencesFromResource(R.xml.preferences)
//
//
//
//        PreferenceManager.setDefaultValues(Objects.requireNonNull<FragmentActivity>(activity), R.xml.preferences, false)
//
//        val preferenceGroup = preferenceScreen.getPreference(0) as PreferenceGroup
//
//        mSourcePref = preferenceGroup.getPreference(0) as MultiSelectListPreference
//
//        initSummary(preferenceScreen)
//    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences,rootKey)
        PreferenceManager.setDefaultValues(Objects.requireNonNull<FragmentActivity>(activity), R.xml.preferences, false)

        val preferenceGroup = preferenceScreen.getPreference(0) as PreferenceGroup

        mSourcePref = preferenceGroup.getPreference(0) as MultiSelectListPreference

        initSummary(preferenceScreen)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(android.R.id.list)
        if (listView != null)
            ViewCompat.setNestedScrollingEnabled(listView, true)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key != getString(R.string.pref_source_key)) {
            updateSummary(findPreference(key))
            updateNightMode(findPreference(key))
            restartActivity()
        } else {
            updateMultiSummary(findPreference(key),
                    sharedPreferences.getStringSet(getString(R.string.pref_source_key), null))
        }
    }

    private fun initSummary(p: Preference) {
        if (p is PreferenceGroup) {
            for (i in 0 until p.preferenceCount) {
                initSummary(p.getPreference(i))
            }
        } else {
            updateSummary(p)
            updateMultiSummary(p, PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull<FragmentActivity>(activity))
                    .getStringSet(getString(R.string.pref_source_key), null))
            updateNightMode(p)
        }
    }

    private fun updateSummary(p: Preference) {
        if (p is ListPreference) {
            p.setSummary(p.entry)

        }
    }

    private fun updateMultiSummary(p: Preference, value: Set<String>?) {
        if (p is MultiSelectListPreference) {

            val entries = ArrayList(value!!)
            val allEntries = StringBuilder()

            for (i in entries.indices) {
                allEntries.append(p.entries[p.findIndexOfValue(entries[i])])
                        .append(", ")
            }

            if (allEntries.isNotEmpty()) {
                allEntries.deleteCharAt(allEntries.length - 2)
            }

            p.setSummary(allEntries.toString())

        }
    }

    private fun updateNightMode(p: Preference) {
        if (p is SwitchPreference) {
            if (p.isChecked) {
                p.setSummary(getString(R.string.pref_theme_checked_summary))
                p.setDefaultValue(getString(R.string.pref_theme_true_value))
            } else {
                p.setSummary(getString(R.string.pref_theme_unchecked_summary))
                p.setDefaultValue(getString(R.string.pref_theme_false_value))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    override fun onStop() {
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onStop()
    }

    private fun restartActivity() {
        val intent = Intent(activity, SettingsActivity::class.java)
        startActivity(intent)
        activity!!.finish()
    }
}
