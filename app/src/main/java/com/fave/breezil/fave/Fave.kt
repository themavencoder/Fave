package com.fave.breezil.fave

import android.app.Activity
import android.app.Application

import com.fave.breezil.fave.di.AppInjector
import javax.inject.Inject

import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector

class Fave: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }

}

