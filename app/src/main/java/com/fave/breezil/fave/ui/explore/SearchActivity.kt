@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.fave.breezil.fave.ui.explore

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.databinding.DataBindingUtil
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import com.fave.breezil.fave.callbacks.ArticleClickListener
import com.fave.breezil.fave.databinding.ActivitySearchBinding
import com.fave.breezil.fave.R

import java.text.SimpleDateFormat
import java.util.Calendar

import javax.inject.Inject

import dagger.android.AndroidInjection

import com.fave.breezil.fave.callbacks.ArticleLongClickListener
import com.fave.breezil.fave.model.Articles
import com.fave.breezil.fave.ui.adapter.ArticleRecyclerViewAdapter
import com.fave.breezil.fave.ui.bottom_sheets.ActionBottomSheetFragment
import com.fave.breezil.fave.ui.bottom_sheets.DescriptionBottomSheetFragment
import com.fave.breezil.fave.ui.main.MainActivity
import com.fave.breezil.fave.ui.main.viewmodel.PreferredViewModel
import com.fave.breezil.fave.ui.preference.SettingsActivity
import com.fave.breezil.fave.utils.Constant.Companion.DEFAULT_SOURCE
import com.fave.breezil.fave.utils.Constant.Companion.ONE
import com.fave.breezil.fave.utils.helpers.BottomNavigationHelper
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding
    internal var adapter: ArticleRecyclerViewAdapter? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: PreferredViewModel
    lateinit var sharedPreferences: SharedPreferences
    internal var source: String? = null

    private var themeMode: Boolean = false




    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        themeMode = sharedPreferences.getBoolean(getString(R.string.pref_theme_key), false)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        if (themeMode) {
            setTheme(R.style.DarkNoActionTheme)
        } else {
            setTheme(R.style.AppNoActionTheme)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PreferredViewModel::class.java)

        setupBottomNavigation()

        binding.searchList.hasFixedSize()

        setUpAdapter()
        setUpViewmodel()

        search()
        val logging = HttpLoggingInterceptor { message -> Timber.tag(getString(R.string.okhttp)).d(message) }
        logging.redactHeader(getString(R.string.authorization))
        logging.redactHeader(getString(R.string.cookie))

    }

    private fun search() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    refresh(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
//                    refresh(newText)
                }
                return true
            }
        })
    }


    private fun setUpAdapter() {

        val articleClickListener = object : ArticleClickListener {
            override fun showDetails(article: Articles) {
                val descriptionBottomSheetFragment = DescriptionBottomSheetFragment.getArticles(article)
                descriptionBottomSheetFragment.show(supportFragmentManager, getString(R.string.show))
            }
        }

        val articleLongClickListener = object : ArticleLongClickListener {
            override fun doSomething(article: Articles) {
                val actionBottomSheetFragment = ActionBottomSheetFragment.getArticles(article)
                actionBottomSheetFragment.show(supportFragmentManager, getString(R.string.show))
            }
        }

        adapter = ArticleRecyclerViewAdapter(this, articleClickListener, articleLongClickListener)

        binding.searchList.adapter = adapter
    }

    fun setUpViewmodel() {
        viewModel.setParameter(getString(R.string.blank), sourceList, getString(R.string.blank), todayDate, twoDaysAgoDate, getString(R.string.blank))

        viewModel.articlesList.observe(this, Observer{ articles ->
            adapter?.submitList(articles)

        })
        viewModel.getNetworkState().observe(this, Observer{ networkState ->
            if (networkState != null) {
                adapter?.setNetworkState(networkState)
            }
        })
    }

    private fun refresh(query: String) {
        viewModel.setParameter(query, sourceList, getString(R.string.blank), todayDate, twoDaysAgoDate, getString(R.string.blank))
        viewModel.refreshArticle().observe(this, Observer {
            adapter?.submitList(it)

        })

        viewModel.getNetworkState().observe(this, Observer {
            if (it != null) {
                adapter?.setNetworkState(it)
            }
        })

    }


    private fun setupBottomNavigation() {


        BottomNavigationHelper.disableShiftMode(binding.bottomNavViewBar)


        val menu = binding.bottomNavViewBar.menu
        val menuItem = menu.getItem(ONE)
        menuItem.isChecked = true


        /*
         * here sets the navigations to its corresponding activities
         */
        binding.bottomNavViewBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.main -> {
                    startActivity(Intent(this@SearchActivity, MainActivity::class.java))
                    finish()
                }
                R.id.search -> {
                }
                R.id.settings -> {
                    startActivity(Intent(this@SearchActivity, SettingsActivity::class.java))
                    finish()
                }
            }


            false
        }

    }

    companion object {


        val twoDaysAgoDate: String
            get() {

                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                val cal = Calendar.getInstance()
                cal.add(Calendar.DATE, -3)
                return dateFormat.format(cal.time)
            }

        val todayDate: String
            get() {

                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                val cal = Calendar.getInstance()
                return dateFormat.format(cal.time)
            }
    }





    private val sourceList: String
        get() {

            val sourceSet = HashSet<String>()
            sourceSet.add(getString(R.string.pref_sources_all_value))

            val entries = ArrayList((
                    sharedPreferences.getStringSet(getString(R.string.pref_source_key), sourceSet)))
            val selectedSources = StringBuilder()

            for (i in entries.indices) {
                selectedSources.append(entries[i]).append(getString(R.string.comma))
            }

            if (selectedSources.isNotEmpty()) {
                selectedSources.deleteCharAt(selectedSources.length - ONE)
            }



            return if (selectedSources.isEmpty()) {
                DEFAULT_SOURCE
            } else {
                selectedSources.toString()

            }

        }




}
