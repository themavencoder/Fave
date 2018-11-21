package com.example.breezil.favnews.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.breezil.favnews.Api.NewsApi;
import com.example.breezil.favnews.db.AppDatabase;
import com.example.breezil.favnews.utils.helpers.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {
    @Singleton
    @Provides
    NewsApi provideNewsApi() {
        return new Retrofit.Builder()
                .baseUrl("https://newsapi.org")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(NewsApi.class);
    }
    @Singleton
    @Provides
    AppDatabase provideDb(Application app) {
        return Room.databaseBuilder(app, AppDatabase.class, "favnew.db").build();
    }
}
