package com.example.breezil.favnews.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.example.breezil.favnews.R;
import com.example.breezil.favnews.databinding.ActivitySettingsBinding;
import com.example.breezil.favnews.utils.helpers.BottomNavigationHelper;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerAppCompatActivity;
import dagger.android.support.HasSupportFragmentInjector;

public class SettingsActivity extends DaggerAppCompatActivity implements HasSupportFragmentInjector {

    ActivitySettingsBinding binding;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_settings);

        setupBottomNavigation();

    }



    private void setupBottomNavigation() {

        BottomNavigationHelper.disableShiftMode(binding.bottomNavViewBar);


        Menu menu = binding.bottomNavViewBar.getMenu();
        MenuItem menuItem= menu.getItem(2);
        menuItem.setChecked(true);


        /*
         * here sets the navigations to its corresponding activities
         */
        binding.bottomNavViewBar.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.main:
                    startActivity(new Intent(SettingsActivity.this,MainActivity.class));
                    break;
                case R.id.search:
                    startActivity(new Intent(SettingsActivity.this,SearchActivity.class));
                    break;
                case R.id.settings:

                    break;
            }


            return false;
        });

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
