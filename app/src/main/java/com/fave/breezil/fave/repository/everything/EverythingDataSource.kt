package com.fave.breezil.fave.repository.everything


import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource

import com.fave.breezil.fave.api.EndPointRepository

import java.util.ArrayList

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

import com.fave.breezil.fave.model.ArticleResult
import com.fave.breezil.fave.model.Articles
import com.fave.breezil.fave.repository.NetworkState
import com.fave.breezil.fave.repository.PaginationListener
import com.fave.breezil.fave.utils.Constant.Companion.ONE
import com.fave.breezil.fave.utils.Constant.Companion.TEN
import com.fave.breezil.fave.utils.Constant.Companion.TWO
import com.fave.breezil.fave.utils.Constant.Companion.ZERO

@Singleton
class EverythingDataSource @Inject
constructor(private var endpointRepository: EndPointRepository,
            private val compositeDisposable: CompositeDisposable
            ) : PageKeyedDataSource<Int, Articles>(), PaginationListener<ArticleResult, Articles> {


    val mNetworkState = MutableLiveData<NetworkState>()
    val mInitialLoading = MutableLiveData<NetworkState>()


    lateinit var query: String
    lateinit var sources: String
    lateinit var sortBy: String
    lateinit var from: String
    lateinit var to: String
    lateinit var language: String



    override fun loadInitial(params: PageKeyedDataSource.LoadInitialParams<Int>, callback: PageKeyedDataSource.LoadInitialCallback<Int, Articles>) {
        mNetworkState.postValue(NetworkState.LOADING)
        mInitialLoading.postValue(NetworkState.LOADING)
        val articlesList = ArrayList<Articles>()
        val articles = endpointRepository.getEverything(query, sources, sortBy, from, to, language,
                TEN, ONE).subscribe({ articleResult -> onInitialSuccess(articleResult, callback, articlesList) }, { throwable -> onInitialError(throwable) })
        compositeDisposable.add(articles)
    }

    override fun loadBefore(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, Articles>) {

    }

    override fun loadAfter(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, Articles>) {
        mNetworkState.postValue(NetworkState.LOADING)
        val articlesList = ArrayList<Articles>()
        val articles = endpointRepository.getEverything(query, sources, sortBy, from, to, language,
                TEN, params.key).subscribe({ articleResult -> onPaginationSuccess(articleResult, callback, params, articlesList) }, { throwable -> onPaginationError(throwable) })
        compositeDisposable.add(articles)
    }

    override fun onInitialError(throwable: Throwable) {
        mInitialLoading.postValue(NetworkState(NetworkState.Status.FAILED))
        mNetworkState.postValue(NetworkState(NetworkState.Status.FAILED))
        Timber.e(throwable)
    }

    override fun onInitialSuccess(response: ArticleResult, callback: PageKeyedDataSource.LoadInitialCallback<Int, Articles>, results: MutableList<Articles>) {
        if (response.articles.size > ZERO) {
            results.addAll(response.articles)
            callback.onResult(results, null,TWO)
            mInitialLoading.postValue(NetworkState.LOADED)
            mNetworkState.postValue(NetworkState.LOADED)

        } else {
            mInitialLoading.postValue(NetworkState(NetworkState.Status.NO_RESULT))
            mNetworkState.postValue(NetworkState(NetworkState.Status.NO_RESULT))
        }
    }

    override fun onPaginationError(throwable: Throwable) {
        mNetworkState.postValue(NetworkState(NetworkState.Status.FAILED))
        Timber.e(throwable)
    }

    override fun onPaginationSuccess(response: ArticleResult, callback:
    PageKeyedDataSource.LoadCallback<Int, Articles>, params: PageKeyedDataSource.LoadParams<Int>, results: MutableList<Articles>) {
        if (response.articles.size > ZERO) {
            results.addAll(response.articles)

            val key = (if (params.key > ONE) params.key + ONE else null)!!.toInt()
            callback.onResult(results, key)

            mNetworkState.postValue(NetworkState.LOADED)
        } else {
            mNetworkState.postValue(NetworkState(NetworkState.Status.NO_RESULT))
        }
    }



    override fun clear() {
        compositeDisposable.clear()
    }
}
