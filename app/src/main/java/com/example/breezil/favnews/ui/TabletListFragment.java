package com.example.breezil.favnews.ui;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.breezil.favnews.R;
import com.example.breezil.favnews.callbacks.AddToBookMarkListener;
import com.example.breezil.favnews.callbacks.ArticleClickListener;
import com.example.breezil.favnews.callbacks.BookMarkClickListener;
import com.example.breezil.favnews.databinding.FragmentTabletListBinding;
import com.example.breezil.favnews.model.Articles;
import com.example.breezil.favnews.model.BookMark;
import com.example.breezil.favnews.ui.adapter.ArticleRecyclerViewAdapter;
import com.example.breezil.favnews.ui.adapter.BookMarkRecyclerAdapter;
import com.example.breezil.favnews.ui.adapter.PreRecyclerAdapter;
import com.example.breezil.favnews.utils.Constant;
import com.example.breezil.favnews.view_model.BookMarkViewModel;
import com.example.breezil.favnews.view_model.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabletListFragment extends Fragment {

    FragmentTabletListBinding binding;
    ArticleRecyclerViewAdapter adapter;
    PreRecyclerAdapter articleRecyclerViewAdapter;
    BookMarkRecyclerAdapter bookMarkRecyclerAdapter;
    SharedPreferences sharedPreferences;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private BookMarkViewModel bookMarkViewModel;


    String source;

    String fragmentType;

    public TabletListFragment() {
        // Required empty public constructor
    }

    public static TabletListFragment getType(String type){
        TabletListFragment fragment = new TabletListFragment();
        Bundle args = new Bundle();
        args.putString(Constant.FRAGMENT_TYPE, type);
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_tablet_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentType = getArguments().getString(Constant.FRAGMENT_TYPE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());


        binding.tabletList.setHasFixedSize(true);
       getListToDisplay(fragmentType);


    }

    private void getListToDisplay(String fragmentType) {

        setUpAdapter();

        if(fragmentType.equals(Constant.PREFERED_FRAGEMENT)){
            getPreferred();
        }else if(fragmentType.equals(Constant.HEADLINE_FRAGMENT)){
            getHeadLine();
        }else if(fragmentType.equals(Constant.BOOKMARK_FRAGMENT)){
            getBookMark();
        }
    }
    private void setUpAdapter(){

        if(fragmentType.equals(Constant.BOOKMARK_FRAGMENT)){
            BookMarkClickListener bookMarkClickListener = bookMark -> {
                BookMarkDetailsFragment bookMarkDetailsFragment = BookMarkDetailsFragment.getBookMark(bookMark.getId());
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detailContainer,bookMarkDetailsFragment)
                        .commit();
            };
            bookMarkRecyclerAdapter = new BookMarkRecyclerAdapter(getContext(),bookMarkClickListener);
            binding.tabletList.setAdapter(bookMarkRecyclerAdapter);
        }else if(fragmentType.equals(Constant.PREFERED_FRAGEMENT)){
            ArticleClickListener articleClickListener = article -> {
                ArticleDetailFragment tabletDetailFragment = ArticleDetailFragment.getArticle(article.getSource().getName(),article);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detailContainer,tabletDetailFragment)
                        .commit();
            };

            AddToBookMarkListener addToBookMarkListener = article -> {
                showAddBookMarkDialog(article);
            };
            articleRecyclerViewAdapter = new PreRecyclerAdapter(getContext(), articleClickListener,addToBookMarkListener);

            binding.tabletList.setAdapter(articleRecyclerViewAdapter);
        }else if(fragmentType.equals(Constant.HEADLINE_FRAGMENT)) {
            ArticleClickListener articleClickListener = article -> {
                ArticleDetailFragment tabletDetailFragment = ArticleDetailFragment.getArticle(article.getSource().getName(),article);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detailContainer,tabletDetailFragment)
                        .commit();
            };

            AddToBookMarkListener addToBookMarkListener = article -> {
                showAddBookMarkDialog(article);
            };
            adapter = new ArticleRecyclerViewAdapter(getContext(), articleClickListener,addToBookMarkListener);

            binding.tabletList.setAdapter(adapter);
        }

    }

    private void getPreferred() {

        String sortBy = sharedPreferences.getString(getString(R.string.pref_everything_sort_by_key),null);

        MainViewModel preferredViewModel = ViewModelProviders.of(this,viewModelFactory).get(MainViewModel.class);
        preferredViewModel.getEverything(null,getSourceList(),null,sortBy,
                getTodayDate(), getTwoDaysAgoDate(), null, Constant.API_KEY)
                .observe(this, articles -> {
                    Toast.makeText(getContext(),String.valueOf(articles.size()),Toast.LENGTH_LONG).show();
                    articleRecyclerViewAdapter.submitList(articles);
                });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("source",source);
        editor.apply();
    }

    public void getHeadLine(){
        MainViewModel headViewModel = ViewModelProviders.of(this,viewModelFactory).get(MainViewModel.class);
        headViewModel.getHeadlines(null,getSourceList(),null,null,
                Constant.API_KEY,1).observe(this, articles -> {
            adapter.submitList(articles);
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("source",source);
        editor.apply();

    }
    public void getBookMark(){
        bookMarkViewModel = ViewModelProviders.of(this).get(BookMarkViewModel.class);
        bookMarkViewModel.getBookmarkList().observe(this, bookMarks -> {
            bookMarkRecyclerAdapter.submitList(bookMarks);
        });
    }

    public static String getTwoDaysAgoDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        return dateFormat.format(cal.getTime());
    }

    public static String getTodayDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
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
