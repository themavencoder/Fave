package com.example.breezil.favnews.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.breezil.favnews.R;
import com.example.breezil.favnews.callbacks.AddToBookMarkListener;
import com.example.breezil.favnews.callbacks.ArticleClickListener;
import com.example.breezil.favnews.databinding.ArticleItemBinding;
import com.example.breezil.favnews.model.Articles;

import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.SearchHolder> {

    Context context;
    private List<Articles> articlesList;
    private AddToBookMarkListener addToBookMarkListener;
    private ArticleClickListener articleClickListener;
    ArticleItemBinding binding;

    public SearchRecyclerAdapter(Context context, List<Articles> articlesList, AddToBookMarkListener addToBookMarkListener,
                                 ArticleClickListener articleClickListener) {
        this.context = context;
        this.articlesList = articlesList;
        this.addToBookMarkListener = addToBookMarkListener;
        this.articleClickListener = articleClickListener;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = ArticleItemBinding.inflate(layoutInflater,parent,false);

        return new SearchHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        Articles articles = articlesList.get(position);
        holder.bind(articles, articleClickListener,addToBookMarkListener);

    }

    @Override
    public int getItemCount() {
        if(articlesList == null){
            return 0;
        }
        return articlesList.size();
    }

//    public void setList(List<Articles> list){
//
//            notifyDataSetChanged();
//            articlesList = list;

//
//
//    }

//    final EmployeeDiffCallback diffCallback = new EmployeeDiffCallback(this.mEmployees, employees);
//    final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
//
//        this.mEmployees.clear();
//        this.mEmployees.addAll(employees);
//        diffResult.dispatchUpdatesTo(this);

    public void updateSearch(List<Articles> list){
        DiffUtils diffUtils = new DiffUtils(this.articlesList,list);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtils);

        this.articlesList.clear();
        this.articlesList.addAll(list);
        diffResult.dispatchUpdatesTo(this);
    }

    class SearchHolder extends RecyclerView.ViewHolder{

        ArticleItemBinding binding;
        public SearchHolder(ArticleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Articles articles, ArticleClickListener articleClickListener,AddToBookMarkListener addToBookMarkListener){
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

            binding.sourcesText.setText(articles.getSource().getName());

        }
    }



}
