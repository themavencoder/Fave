package com.example.breezil.favnews.ui;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import com.example.breezil.favnews.R;
import com.example.breezil.favnews.callbacks.AddToBookMarkListener;
import com.example.breezil.favnews.callbacks.ArticleClickListener;
import com.example.breezil.favnews.databinding.ActivitySearchBinding;
import com.example.breezil.favnews.model.Articles;
import com.example.breezil.favnews.model.BookMark;
import com.example.breezil.favnews.ui.adapter.ArticleRecyclerViewAdapter;
import com.example.breezil.favnews.ui.adapter.SearchRecyclerAdapter;
import com.example.breezil.favnews.utils.Constant;
import com.example.breezil.favnews.utils.helpers.BottomNavigationHelper;
import com.example.breezil.favnews.view_model.BookMarkViewModel;
import com.example.breezil.favnews.view_model.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    SearchRecyclerAdapter adapter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    MainViewModel viewModel;

    List<Articles> articlesList =new  ArrayList<>();
    private static final String fragmentType = "Search Result";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search);
        setupBottomNavigation();

        binding.searchList.hasFixedSize();
        binding.searchView.setQueryHint(Constant.SEARCH_HINT);


        setUpAdapter();
        search();



    }

    private void search() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                    setUpViewmodel(query);
                    adapter.notifyDataSetChanged();


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setupBottomNavigation() {


        BottomNavigationHelper.disableShiftMode( binding.bottomNavViewBar);


        Menu menu =  binding.bottomNavViewBar.getMenu();
        MenuItem menuItem= menu.getItem(1);
        menuItem.setChecked(true);


        /*
         * here sets the navigations to its corresponding activities
         */
        binding.bottomNavViewBar.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.main:
                    startActivity(new Intent(SearchActivity.this,MainActivity.class));
                    break;
                case R.id.search:

                    break;
                case R.id.settings:
                    startActivity(new Intent(SearchActivity.this,SettingsActivity.class));
                    break;
            }


            return false;
        });

    }

    private void setUpAdapter(){
        ArticleClickListener articleClickListener = article -> {
            Intent intent = new Intent(this,ArticleDetailActivity.class);
            intent.putExtra(Constant.ARTICLE,article);
            intent.putExtra(Constant.SOURCENAME,article.getSource().getName());
            intent.putExtra(Constant.SEARCH_RESULT,fragmentType);
            startActivity(intent);
        };
        AddToBookMarkListener addToBookMarkListener = article -> {
            showAddBookMarkDialog(article);
        };
        adapter = new SearchRecyclerAdapter(this, articlesList, addToBookMarkListener, articleClickListener);

        binding.searchList.setAdapter(adapter);
    }

    public void setUpViewmodel(String query){
        adapter.notifyDataSetChanged();
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(MainViewModel.class);
        viewModel.getEverything(query,null,null,null,
                getTodayDate(), getTwoDaysAgoDate(), null, Constant.API_KEY)
                .observe(this, articles -> {
                    adapter.updateSearch(articles);
                });
    }

    public static String getTwoDaysAgoDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);
        return dateFormat.format(cal.getTime());
    }

    public static String getTodayDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    private void showAddBookMarkDialog(Articles articles) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Are you sure, you want to add this to bookmarks?").
                setPositiveButton("Yes", (dialog, which) -> {
                    BookMarkViewModel bookMarkViewModel =  ViewModelProviders.of(this,viewModelFactory).get(BookMarkViewModel.class);

                    BookMark bookMark = new BookMark(articles.getAuthor(),
                            articles.getTitle(),articles.getDescription(),
                            articles.getUrl(),articles.getUrlToImage(),
                            articles.getPublishedAt(),articles.getSource().getName(),
                            articles.getContent());

                    bookMarkViewModel.insert(bookMark);

                    dialog.dismiss();

                    Toast.makeText(this, "Added to bookmark", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Add to BookMark");
        alertDialog.show();
    }

    public void clearData(){

        adapter.notifyDataSetChanged();
    }




}
