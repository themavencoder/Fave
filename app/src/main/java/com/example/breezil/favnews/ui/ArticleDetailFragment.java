package com.example.breezil.favnews.ui;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.breezil.favnews.R;
import com.example.breezil.favnews.databinding.FragmentArticleDetailBinding;
import com.example.breezil.favnews.model.Articles;
import com.example.breezil.favnews.utils.Constant;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleDetailFragment extends Fragment {

    Articles article;
    String source;
    FragmentArticleDetailBinding binding;
    boolean isTablet;

    public ArticleDetailFragment() {
        // Required empty public constructor
    }
    public static ArticleDetailFragment getArticle(String source, Articles article){
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.ARTICLE, article);
        args.putString(Constant.SOURCENAME, source);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_article_detail, container, false);


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        article = getArguments().getParcelable(Constant.ARTICLE);
        source = getArguments().getString(Constant.SOURCENAME);
        updateUi(getArticle());
    }

    private void updateUi(Articles article) {
        Glide.with(this)
                .load(article.getUrlToImage())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder))
                .into(binding.backDropImage);

        binding.articleAuthor.setText(article.getAuthor());
        binding.articleDate.setText(article.getPublishedAt());
        binding.articleSource.setText(source);
        binding.shortStory.setText(article.getDescription());
        binding.fullStory.setText(article.getContent());
        binding.collapsingToolbar.setTitle(article.getTitle());

        isTablet = getResources().getBoolean(R.bool.is_tablet);
        if(!isTablet){
            binding.detailBar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
            binding.detailBar.setNavigationOnClickListener(v -> {
                getActivity().onBackPressed();
            });
        }




    }


    private Articles getArticle(){

        if(getArguments().getParcelable(Constant.ARTICLE) != null){
            return getArguments().getParcelable(Constant.ARTICLE);

        }else{
            return null;
        }
    }

}
