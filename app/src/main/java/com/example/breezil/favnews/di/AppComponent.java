package com.example.breezil.favnews.di;

import android.app.Application;

import com.example.breezil.favnews.FavNewsApp;
import com.example.breezil.favnews.di.module.AppModule;
import com.example.breezil.favnews.di.module.ArticleDetailActivityModule;
import com.example.breezil.favnews.di.module.MainActivityModule;
import com.example.breezil.favnews.di.module.SearchActivityModule;
import com.example.breezil.favnews.di.module.SettingsActivityModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        MainActivityModule.class,
        ArticleDetailActivityModule.class,
        SearchActivityModule.class,
        SettingsActivityModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(FavNewsApp favNewsApp);
}
