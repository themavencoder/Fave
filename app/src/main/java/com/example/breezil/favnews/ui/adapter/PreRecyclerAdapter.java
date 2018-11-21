package com.example.breezil.favnews.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.breezil.favnews.R;
import com.example.breezil.favnews.callbacks.AddToBookMarkListener;
import com.example.breezil.favnews.callbacks.ArticleClickListener;
import com.example.breezil.favnews.databinding.ArticleItemBinding;
import com.example.breezil.favnews.model.Articles;

public class PreRecyclerAdapter extends ListAdapter<Articles,PreRecyclerAdapter.ArticleHolder>{


    Context context;
    private ArticleClickListener articleClickListener;
    private AddToBookMarkListener addToBookMarkListener;

//    private List<Articles> articlesList;

    ArticleItemBinding binding;

    public PreRecyclerAdapter(Context context, ArticleClickListener articleClickListener ,
                                      AddToBookMarkListener addToBookMarkListener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.articleClickListener = articleClickListener;
        this.addToBookMarkListener = addToBookMarkListener;
    }

    private static final DiffUtil.ItemCallback<Articles> DIFF_CALLBACK = new DiffUtil.ItemCallback<Articles>() {
        @Override
        public boolean areItemsTheSame(Articles oldItem, Articles newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }

        @Override
        public boolean areContentsTheSame(Articles oldItem, Articles newItem) {
            return oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getPublishedAt().equals(newItem.getPublishedAt());
        }
    };



    public Articles getArticlesAt(int position){
        return getItem(position);
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = ArticleItemBinding.inflate(layoutInflater,parent,false);



        return new PreRecyclerAdapter.ArticleHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
        Articles articles = getItem(position);
        holder.bind(articles, articleClickListener,addToBookMarkListener);

    }

    class ArticleHolder extends RecyclerView.ViewHolder{

        ArticleItemBinding binding;
        public ArticleHolder(ArticleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Articles articles, ArticleClickListener articleClickListener, AddToBookMarkListener addToBookMarkListener){
            itemView.setOnClickListener(v -> {
                articleClickListener.showDetails(articles);
            });
            itemView.setOnLongClickListener(v -> {
                addToBookMarkListener.addtoBookMark(articles);
                return true;
            });

            binding.articleTitle.setText(articles.getTitle());
            binding.articleDate.setText(articles.getPublishedAt());

            Glide.with(context)
                    .load(articles.getUrlToImage())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder))
                    .into(binding.articleImage);

            if(articles.getSource() != null){
                binding.sourcesText.setText(articles.getSource().getName());
            }


        }
    }

}
