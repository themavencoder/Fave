package com.example.breezil.favnews.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.breezil.favnews.R;
import com.example.breezil.favnews.databinding.ActivityMainBinding;
import com.example.breezil.favnews.ui.adapter.PagerAdapter;
import com.example.breezil.favnews.utils.helpers.BottomNavigationHelper;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerAppCompatActivity;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends DaggerAppCompatActivity implements HasSupportFragmentInjector {


    @Inject
    ViewModelProvider.Factory viewModelFactory;
    ActivityMainBinding binding;


    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    PagerAdapter pagerAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);


        setupAdapter();
        setupBottomNavigation();
        setSupportActionBar((Toolbar) binding.mainToolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    private void setupBottomNavigation() {

        BottomNavigationHelper.disableShiftMode(binding.bottomNavViewBar);


        Menu menu = binding.bottomNavViewBar.getMenu();
        MenuItem menuItem= menu.getItem(0);
        menuItem.setChecked(true);


        /*
         * here sets the navigations to its corresponding activities
         */
        binding.bottomNavViewBar.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.main:

                    break;
                case R.id.search:
                    startActivity(new Intent(MainActivity.this,SearchActivity.class));
                    break;
                case R.id.settings:
                    startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                    break;
            }


            return false;
        });

    }


    void setupAdapter(){
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), this);
        binding.container.setAdapter(pagerAdapter);
        binding.tabs.setupWithViewPager(binding.container);
        binding.container.setCurrentItem(1,false);

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
