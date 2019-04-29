@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

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
import android.widget.Toast

import com.example.breezil.fave.R
import com.example.breezil.fave.callbacks.ArticleLongClickListener
import com.example.breezil.fave.callbacks.ArticleClickListener
import com.example.breezil.fave.databinding.FragmentHeadlinesBinding
import com.example.breezil.fave.model.Articles
import com.example.breezil.fave.ui.adapter.ArticleRecyclerViewAdapter
import com.example.breezil.fave.ui.bottom_sheets.ActionBottomSheetFragment
import com.example.breezil.fave.ui.bottom_sheets.DescriptionBottomSheetFragment
import com.example.breezil.fave.ui.main.viewmodel.HeadlineViewModel
import com.example.breezil.fave.utils.Constant.Companion.DEFAULT_SOURCE

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection


/**
 * A simple [Fragment] subclass.
 */
class HeadlinesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var articleAdapter: ArticleRecyclerViewAdapter? = null
    lateinit var binding: FragmentHeadlinesBinding
    lateinit var viewModel: HeadlineViewModel
    lateinit var sharedPreferences: SharedPreferences

    private var source: String = ""


    private val sourceList: String
        get() {

            val sourceSet = HashSet<String>()
            sourceSet.add(getString(R.string.pref_sources_all_value))

            val entries = ArrayList((
                    sharedPreferences.getStringSet(getString(R.string.pref_source_key), sourceSet)))
            val selectedSources = StringBuilder()

            for (i in entries.indices) {
                selectedSources.append(entries[i]).append(",")
            }

            if (selectedSources.isNotEmpty()) {
                selectedSources.deleteCharAt(selectedSources.length - 1)
            }



            return if (selectedSources.isEmpty()) {
                DEFAULT_SOURCE
            } else {
               selectedSources.toString()

            }

        }





    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_headlines, container, false)
        binding.headlineList.setHasFixedSize(true)

        if (internetConnected()) {
            binding.swipeRefresh.setOnRefreshListener { this.refresh() }
        }


        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HeadlineViewModel::class.java)


        setUpAdapter()
        setupViewModel()

    }

    private fun setUpAdapter() {

        val articleClickListener = object : ArticleClickListener {
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

        binding.headlineList.adapter = articleAdapter
    }

    private fun setupViewModel() {

        if (internetConnected()) {
            binding.swipeRefresh.visibility = View.VISIBLE
            binding.swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary,
                    R.color.colorblue, R.color.hotPink)
            viewModel.deleteAllInDb()

            viewModel.setParameter("", sourceList, "", "")
            viewModel.articlesList.observe(this, Observer { articles
                ->
                articleAdapter!!.submitList(articles)
//                if(articles!!.size > 0){
//                    articleAdapter!!.submitList(articles)
//                }else{
//                    Toast.makeText(context, "Loading wait ...", Toast.LENGTH_LONG).show()
//                }


            })

            viewModel.getNetworkState().observe(this, Observer{ networkState ->
                if (networkState != null) {
                    articleAdapter!!.setNetworkState(networkState)
                }
            })
        } else {
            viewModel.fromDbList.observe(this, Observer{ articles ->
                binding.swipeRefresh.visibility = View.GONE
                articleAdapter!!.submitList(articles)
            })
        }


        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = preferences.edit()
        editor.putString("source", source)
        editor.apply()


        binding.swipeRefresh.isRefreshing = false
    }

    private fun refresh() {
        viewModel.setParameter("", sourceList, "", "")
        viewModel.refreshArticle().observe(this, Observer
                { articles -> articleAdapter!!.submitList(articles) })
        binding.swipeRefresh.isRefreshing = false

        viewModel.getNetworkState().observe(this, Observer { networkState ->
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
}// Required empty public constructor
