package com.example.breezil.favnews.ui;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.breezil.favnews.databinding.FragmentBookMarksBinding;
import com.example.breezil.favnews.R;
import com.example.breezil.favnews.callbacks.BookMarkClickListener;
import com.example.breezil.favnews.ui.adapter.BookMarkRecyclerAdapter;
import com.example.breezil.favnews.utils.Constant;
import com.example.breezil.favnews.utils.helpers.GridLayoutHelper;
import com.example.breezil.favnews.view_model.BookMarkViewModel;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookMarksFragment extends Fragment {

    BookMarkRecyclerAdapter adapter;
    private BookMarkViewModel bookMarkViewModel;

    FragmentBookMarksBinding binding;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private static final String fragmentType = "BookMarks";
    boolean isTablet;

    public BookMarksFragment() {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_marks, container, false);
        binding.bookmarkList.setHasFixedSize(true);
        isTablet = getResources().getBoolean(R.bool.is_tablet);
        if(isTablet){
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), GridLayoutHelper.calculateNoOfColumns(getActivity()));
            binding.bookmarkList.setLayoutManager(layoutManager);
        }

        binding.deleteAll.setOnClickListener(v -> {
            showDeleteAllDialog();
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpAdapter();
        setUpViewModel();
        swipeDelete();
    }

    private void setUpAdapter(){
        BookMarkClickListener bookMarkClickListener = bookMark -> {
            Intent intent = new Intent(getActivity(),ArticleDetailActivity.class);
            intent.putExtra(Constant.BOOKMARK_ID,bookMark.getId());
            intent.putExtra(Constant.SOURCENAME,bookMark.getSource());
            intent.putExtra(Constant.FRAGMENT_TYPE,fragmentType);
            startActivity(intent);
        };
        adapter = new BookMarkRecyclerAdapter(getContext(),bookMarkClickListener);
        binding.bookmarkList.setAdapter(adapter);




    }

    private void setUpViewModel(){
        bookMarkViewModel = ViewModelProviders.of(this).get(BookMarkViewModel.class);
        bookMarkViewModel.getBookmarkList().observe(this, bookMarks -> {
            adapter.submitList(bookMarks);
        });
    }

    private void swipeDelete(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                showDeleteDialog(viewHolder);
            }
        }).attachToRecyclerView(binding.bookmarkList);
    }

    private void showDeleteDialog(RecyclerView.ViewHolder viewHolder) {



        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setMessage("Are you sure, you want to delete this?").
                setPositiveButton("Yes", (dialog, which) -> {
                    bookMarkViewModel.delete(adapter.getBookMarkAt(viewHolder.getAdapterPosition()));
                    dialog.dismiss();

                    Toast.makeText(getActivity(), "BookMark Deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Delete BookMark");
        alertDialog.show();

    }

    private void showDeleteAllDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setMessage("Are you sure, you want to delete all bookmarks?").
                setPositiveButton("Yes", (dialog, which) -> {
                    deleteAll();
                    dialog.dismiss();

                    Toast.makeText(getActivity(), "BookMark list emptied", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Delete All");
        alertDialog.show();

    }

    private void deleteAll(){
        bookMarkViewModel.deleteAll();
    }



}
