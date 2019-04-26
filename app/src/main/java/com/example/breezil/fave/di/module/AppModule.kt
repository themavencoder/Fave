package com.example.breezil.fave.di.module

import android.app.Application
import android.arch.persistence.room.Room

import com.example.breezil.fave.api.NewsApi
import com.example.breezil.fave.api.OkHttp
import com.example.breezil.fave.db.AppDatabase
import com.example.breezil.fave.utils.Constant

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    internal fun provideNewsApi(): NewsApi {
        val okHttp = OkHttp()
        return Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttp.client)
                .build()
                .create(NewsApi::class.java)
    }

    @Singleton
    @Provides
    internal fun provideDb(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "favnew.db").build()
    }

    @Provides
    internal fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

}
