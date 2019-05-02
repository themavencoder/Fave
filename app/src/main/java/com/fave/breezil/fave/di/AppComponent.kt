package com.fave.breezil.fave.di

import android.app.Application

import com.fave.breezil.fave.Fave
import com.fave.breezil.fave.di.module.AppModule
import com.fave.breezil.fave.di.module.MainActivityModule
import com.fave.breezil.fave.di.module.SearchActivityModule
import com.fave.breezil.fave.di.module.SettingsActivityModule

import javax.inject.Singleton

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule

@Singleton
@Component(modules = [AndroidInjectionModule::class,
    AppModule::class,
    MainActivityModule::class,
    SearchActivityModule::class,
    SettingsActivityModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(fave: Fave)
}
