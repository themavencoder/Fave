package com.example.breezil.favnews.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.breezil.favnews.R;
import com.example.breezil.favnews.databinding.ActivityArticleDetailBinding;
import com.example.breezil.favnews.model.Articles;
import com.example.breezil.favnews.model.BookMark;
import com.example.breezil.favnews.ui.adapter.ArticleRecyclerViewAdapter;
import com.example.breezil.favnews.utils.Constant;
import com.example.breezil.favnews.view_model.BookMarkViewModel;
import com.example.breezil.favnews.view_model.DetailViewModel;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerAppCompatActivity;
import dagger.android.support.HasSupportFragmentInjector;

import static com.example.breezil.favnews.utils.Constant.BOOKMARK_FRAGMENT;
import static com.example.breezil.favnews.utils.Constant.HEADLINE_FRAGMENT;
import static com.example.breezil.favnews.utils.Constant.PREFERED_FRAGEMENT;
import static com.example.breezil.favnews.utils.Constant.SEARCH_RESULT;

public class ArticleDetailActivity extends DaggerAppCompatActivity implements HasSupportFragmentInjector {

    ActivityArticleDetailBinding binding;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    DetailViewModel viewModel;
    BookMarkViewModel bookMarkViewModel;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    String source;
    boolean isTablet;
    Articles article;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_article_detail);

        isTablet = getResources().getBoolean(R.bool.is_tablet);
        if(isTablet){

            String type = getIntent().getStringExtra(Constant.FRAGMENT_TYPE);
            getFragmentType(type);
            tablet_fragment(type);

        }




        if(getIntent().hasExtra(Constant.BOOKMARK_ID)){
            int bookMarkId  = getIntent().getIntExtra(Constant.BOOKMARK_ID,0);
            BookMarkDetailsFragment bookMarkDetailsFragment = BookMarkDetailsFragment.getBookMark(bookMarkId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detailContainer,bookMarkDetailsFragment)
                    .commit();
        }else{
            source = getIntent().getStringExtra(Constant.SOURCENAME);
            ArticleDetailFragment articleDetailFragment = ArticleDetailFragment.getArticle(source,getArticle());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.detailContainer,articleDetailFragment)
                    .commit();
        }



    }

    private void tablet_fragment(String type) {

        if(type.equals(SEARCH_RESULT)){
            TabletSearchFragment tabletSearchFragment = TabletSearchFragment.getType(type);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,tabletSearchFragment)
                    .commit();
        }else {
            TabletListFragment tabletListFragment = TabletListFragment.getType(type);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,tabletListFragment)
                    .commit();
        }


    }



    private void getFragmentType(String type) {


            if(binding.tableViewToolbar != null){
                setSupportActionBar((Toolbar) binding.tableViewToolbar);
            }

            if(type.equals(Constant.SEARCH_RESULT)){
                getSupportActionBar().setTitle(SEARCH_RESULT);

            }

            else if(type.equals(Constant.PREFERED_FRAGEMENT)){
                getSupportActionBar().setTitle(PREFERED_FRAGEMENT);
            }else if(type.equals(Constant.HEADLINE_FRAGMENT)){
                getSupportActionBar().setTitle(HEADLINE_FRAGMENT);

            }else if(type.equals(Constant.BOOKMARK_FRAGMENT)){
                getSupportActionBar().setTitle(BOOKMARK_FRAGMENT);

            }

    }



    private Articles getArticle(){
        Intent intent = this.getIntent();
        if(intent.hasExtra(Constant.ARTICLE)){
            return intent.getParcelableExtra(Constant.ARTICLE);

        }else{
            return null;
        }
    }





    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }


}
