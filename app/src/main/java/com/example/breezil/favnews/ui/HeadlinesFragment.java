package com.example.breezil.favnews.ui;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.breezil.favnews.R;
import com.example.breezil.favnews.callbacks.AddToBookMarkListener;
import com.example.breezil.favnews.callbacks.ArticleClickListener;
import com.example.breezil.favnews.databinding.FragmentHeadlinesBinding;
import com.example.breezil.favnews.model.Articles;
import com.example.breezil.favnews.model.BookMark;
import com.example.breezil.favnews.ui.adapter.ArticleRecyclerViewAdapter;
import com.example.breezil.favnews.utils.Constant;
import com.example.breezil.favnews.utils.helpers.GridLayoutHelper;
import com.example.breezil.favnews.view_model.BookMarkViewModel;
import com.example.breezil.favnews.view_model.MainViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static java.security.AccessController.getContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeadlinesFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ArticleRecyclerViewAdapter articleAdapter;
    FragmentHeadlinesBinding binding;
    MainViewModel viewModel;
    private SharedPreferences sharedPreferences;
    String source;
    boolean isTablet;



    private static final String fragmentType = "Top Stories";


    public HeadlinesFragment() {
        // Required empty public constructor
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_headlines, container, false);
        binding.headlineList.setHasFixedSize(true);
        isTablet = getResources().getBoolean(R.bool.is_tablet);
        if(isTablet){
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), GridLayoutHelper.calculateNoOfColumns(getActivity()));
            binding.headlineList.setLayoutManager(layoutManager);
        }

        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());


        setUpAdapter();
        setupViewModel();
//        swipeAddToBookMark();

    }

    private void setUpAdapter(){
        ArticleClickListener articleClickListener = article -> {
            Intent intent = new Intent(getActivity(),ArticleDetailActivity.class);
            intent.putExtra(Constant.ARTICLE,article);
            intent.putExtra(Constant.SOURCENAME,article.getSource().getName());
            intent.putExtra(Constant.FRAGMENT_TYPE,fragmentType);

            startActivity(intent);
        };

        AddToBookMarkListener addToBookMarkListener = article -> {
            showAddBookMarkDialog(article);
        };

        articleAdapter = new ArticleRecyclerViewAdapter(getContext(), articleClickListener,addToBookMarkListener);

        binding.headlineList.setAdapter(articleAdapter);
    }

    private void setupViewModel(){
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(MainViewModel.class);
        viewModel.getHeadlines(null,getSourceList(),null,null, Constant.API_KEY,1).observe(this, articles -> {
            articleAdapter.submitList(articles);
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("source",source);
        editor.apply();
    }



    private void showAddBookMarkDialog(Articles articles) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage("Are you sure, you want to add this to bookmarks?").
                setPositiveButton("Yes", (dialog, which) -> {
                    BookMarkViewModel bookMarkViewModel =  ViewModelProviders.of(this,viewModelFactory).get(BookMarkViewModel.class);
//                    Articles articles = articleAdapter.getArticlesAt(viewHolder.getAdapterPosition());


                    BookMark bookMark = new BookMark(articles.getAuthor(),
                            articles.getTitle(),articles.getDescription(),
                            articles.getUrl(),articles.getUrlToImage(),
                            articles.getPublishedAt(),articles.getSource().getName(),
                            articles.getContent());

                    bookMarkViewModel.insert(bookMark);


                    dialog.dismiss();

                    Toast.makeText(getActivity(), "Added to bookmark", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Add to BookMark");
        alertDialog.show();
    }


    public String getSourceList(){
        Set<String> sourceSet = new HashSet<>();
        sourceSet.add(getString(R.string.pref_sources_all_value));

        List<String> entries = new ArrayList<>(Objects.requireNonNull(
                sharedPreferences.getStringSet(getString(R.string.pref_source_key), sourceSet)));
        StringBuilder selectedSources = new StringBuilder();

        for (int i = 0; i < entries.size(); i++) {
            selectedSources.append(entries.get(i)).append(",");
        }

        if (selectedSources.length() > 0) {
            selectedSources.deleteCharAt(selectedSources.length() - 1);
        }


        if(selectedSources.toString().equals("")){
            return source = "bbc-news";
        }else{
            return source = selectedSources.toString();

        }

    }
}
