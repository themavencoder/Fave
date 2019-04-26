package com.example.breezil.fave.repository.everything

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource

import com.example.breezil.fave.model.Articles

import javax.inject.Inject

class EverythingDataSourceFactory @Inject
constructor(val everythingDataSource: EverythingDataSource) : DataSource.Factory<Int, Articles>() {
    val everythingDataSourceMutableLiveData: MutableLiveData<EverythingDataSource> = MutableLiveData()

    override fun create(): DataSource<Int, Articles> {
        everythingDataSourceMutableLiveData.postValue(everythingDataSource)
        return everythingDataSource
    }
    fun getMutableEverythingDataSourceMutableLivate():MutableLiveData<EverythingDataSource>{
        return everythingDataSourceMutableLiveData
    }
}
