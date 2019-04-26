package com.example.breezil.fave.ui.main.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.breezil.fave.R
import com.example.breezil.fave.callbacks.ArticleLongClickListener
import com.example.breezil.fave.callbacks.ArticleClickListener
import com.example.breezil.fave.databinding.FragmentPreferredBinding
import com.example.breezil.fave.model.Articles
import com.example.breezil.fave.ui.adapter.ArticleRecyclerViewAdapter
import com.example.breezil.fave.ui.bottom_sheets.ActionBottomSheetFragment
import com.example.breezil.fave.ui.bottom_sheets.DescriptionBottomSheetFragment
import com.example.breezil.fave.ui.main.viewmodel.PreferredViewModel
import com.example.breezil.fave.utils.Constant.Companion.DEFAULT_SOURCE

import java.text.SimpleDateFormat
import java.util.Calendar

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection

class PreferredFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var  articleAdapter: ArticleRecyclerViewAdapter? = null
    lateinit var binding: FragmentPreferredBinding
    lateinit var viewModel: PreferredViewModel
    lateinit var sharedPreferences: SharedPreferences
    internal var source: String? = null


    internal var sortBy: String? = null

//    val sourceList: String
//        get() {
//            val sourceSet = HashSet<String>()
//            sourceSet.add(getString(R.string.pref_sources_all_value))
//
//            val entries = ArrayList(Objects.requireNonNull(
//                    sharedPreferences.getStringSet(getString(R.string.pref_source_key), sourceSet)))
//            val selectedSources = StringBuilder()
//
//            for (i in entries.indices) {
//                selectedSources.append(entries[i]).append(",")
//            }
//
//            if (selectedSources.length > 0) {
//                selectedSources.deleteCharAt(selectedSources.length - 1)
//            }
//
//
//            return if (selectedSources.toString() == "") {
//                source = "bbc-news,axios,cnn,daily-mail,espn"
//            } else {
//                source = selectedSources.toString()
//
//            }
//
//        }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_preferred, container, false)
        binding.preferredList.setHasFixedSize(true)
        if (internetConnected()) {
            binding.swipeRefresh.setOnRefreshListener({ this.refresh() })
        }
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PreferredViewModel::class.java)
        sortBy = sharedPreferences.getString(getString(R.string.pref_everything_sort_by_key), "")


        setUpAdapter()
        setupViewModel()
    }

    private fun setUpAdapter() {

        val articleClickListener = object : ArticleClickListener{
            override fun showDetails(article: Articles) {
                val descriptionBottomSheetFragment = DescriptionBottomSheetFragment.getArticles(article)
                descriptionBottomSheetFragment.show(fragmentManager!!, "show")
            }
        }

        val articleLongClickListener = object : ArticleLongClickListener{
            override fun doSomething(article: Articles) {
                val actionBottomSheetFragment = ActionBottomSheetFragment.getArticles(article)
                actionBottomSheetFragment.show(fragmentManager!!, "show")
            }
        }



        articleAdapter = ArticleRecyclerViewAdapter(context!!, articleClickListener, articleLongClickListener)

        binding.preferredList.adapter = articleAdapter
    }

    private fun setupViewModel() {


        if (internetConnected()) {
            binding.swipeRefresh.visibility = View.VISIBLE
            binding.swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary,
                    R.color.colorblue, R.color.hotPink)


            viewModel.setParameter("", DEFAULT_SOURCE, sortBy!!, todayDate, twoDaysAgoDate, "")
            viewModel.articlesList.observe(this, Observer{ articles -> articleAdapter!!.submitList(articles) })

            viewModel.getNetworkState().observe(this, Observer{ networkState ->
                if (networkState != null) {
                    articleAdapter!!.setNetworkState(networkState)
                }
            })
        } else {
            binding.swipeRefresh.visibility = View.GONE
        }


        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = preferences.edit()
        editor.putString("source", source)
        editor.apply()

        binding.swipeRefresh.isRefreshing = false
    }


    private fun refresh() {
        viewModel.setParameter("", DEFAULT_SOURCE, sortBy!!, todayDate, twoDaysAgoDate, "")
        viewModel.refreshArticle().observe(this, Observer
                { articles -> articleAdapter!!.submitList(articles) })
        binding.swipeRefresh.isRefreshing = false

        viewModel.getNetworkState().observe(this, Observer{ networkState ->
            if (networkState != null) {
                articleAdapter!!.setNetworkState(networkState)
            }
        })

    }

    private fun internetConnected(): Boolean {
        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    companion object {

        val twoDaysAgoDate: String
            get() {

                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                val cal = Calendar.getInstance()
                cal.add(Calendar.DATE, -2)
                return dateFormat.format(cal.time)
            }

        val todayDate: String
            get() {

                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                val cal = Calendar.getInstance()
                return dateFormat.format(cal.time)
            }
    }

}// Required empty public constructor
