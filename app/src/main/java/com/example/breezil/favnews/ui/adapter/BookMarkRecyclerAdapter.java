package com.example.breezil.favnews.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.breezil.favnews.R;
import com.example.breezil.favnews.callbacks.ArticleClickListener;
import com.example.breezil.favnews.callbacks.BookMarkClickListener;
import com.example.breezil.favnews.databinding.ArticleItemBinding;
import com.example.breezil.favnews.model.BookMark;

public class BookMarkRecyclerAdapter  extends ListAdapter<BookMark,BookMarkRecyclerAdapter.BookMarkHolder>{

    Context context;
    ArticleItemBinding binding;
    private BookMarkClickListener listener;


    public BookMarkRecyclerAdapter( Context context, BookMarkClickListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<BookMark> DIFF_CALLBACK = new DiffUtil.ItemCallback<BookMark>() {
        @Override
        public boolean areItemsTheSame(BookMark oldItem, BookMark newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }

        @Override
        public boolean areContentsTheSame(BookMark oldItem, BookMark newItem) {
            return oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getPublishedAt().equals(newItem.getPublishedAt());
        }
    };

    @NonNull
    @Override
    public BookMarkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = ArticleItemBinding.inflate(layoutInflater,parent,false);
        return new BookMarkHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookMarkHolder holder, int position) {
        BookMark bookMark = getItem(position);
        holder.bind(bookMark, listener);
    }

    public BookMark getBookMarkAt(int position){
        return  getItem(position);
    }

    class BookMarkHolder extends RecyclerView.ViewHolder{
        ArticleItemBinding binding;
        public BookMarkHolder(ArticleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
        public void bind(BookMark bookMark, BookMarkClickListener listener){
            itemView.setOnClickListener(v -> {
                listener.showDetails(bookMark);
            });

            Glide.with(context)
                    .load(bookMark.getUrlToImage())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder))
                    .into(binding.articleImage);


            binding.articleTitle.setText(bookMark.getTitle());
            binding.sourcesText.setText(bookMark.getSource());
            binding.articleDate.setText(bookMark.getPublishedAt());
        }
    }
}
