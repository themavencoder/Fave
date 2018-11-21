package com.example.breezil.favnews.ui;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.breezil.favnews.R;
import com.example.breezil.favnews.databinding.FragmentBookMarkDetailsBinding;
import com.example.breezil.favnews.model.BookMark;
import com.example.breezil.favnews.utils.Constant;
import com.example.breezil.favnews.view_model.BookMarkViewModel;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookMarkDetailsFragment extends Fragment {

    FragmentBookMarkDetailsBinding binding;
    int bookMarkId;
    boolean isTablet;


    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private BookMarkViewModel bookMarkViewModel;

    public BookMarkDetailsFragment() {
        // Required empty public constructor
    }

    public static BookMarkDetailsFragment getBookMark(int bookMarkId){
        BookMarkDetailsFragment fragment = new BookMarkDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.BOOKMARK_ID,bookMarkId);
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
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_book_mark_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bookMarkId = getArguments().getInt(Constant.BOOKMARK_ID);

        updateUi(bookMarkId);

    }

    private void updateUi(int bookMarkId) {
        bookMarkViewModel = ViewModelProviders.of(this).get(BookMarkViewModel.class);
        bookMarkViewModel.getBookMarkById(bookMarkId).observe(this,bookMark -> {
            setFields(bookMark);
        });



    }

    private void setFields(BookMark bookMark){
        Glide.with(this)
                .load(bookMark.getUrlToImage())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder))
                .into(binding.backDropImage);

        binding.articleAuthor.setText(bookMark.getAuthor());
        binding.articleDate.setText(bookMark.getPublishedAt());
        binding.articleSource.setText(bookMark.getSource());
        binding.shortStory.setText(bookMark.getDescription());
        binding.fullStory.setText(bookMark.getContent());
        binding.collapsingToolbar.setTitle(bookMark.getTitle());

        isTablet = getResources().getBoolean(R.bool.is_tablet);
        if(!isTablet){
            binding.detailBar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
            binding.detailBar.setNavigationOnClickListener(v -> {
                getActivity().onBackPressed();
            });
        }

    }



}
