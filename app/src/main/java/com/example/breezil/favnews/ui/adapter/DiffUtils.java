package com.example.breezil.favnews.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.breezil.favnews.model.Articles;

import java.util.List;

public class DiffUtils extends DiffUtil.Callback {

    private List<Articles> oldArticleList;
    private List<Articles> newArticleList;

    public DiffUtils(List<Articles> oldArticleList, List<Articles> newArticleList) {
        this.oldArticleList = oldArticleList;
        this.newArticleList = newArticleList;
    }

    @Override
    public int getOldListSize() {
        return oldArticleList.size();
    }

    @Override
    public int getNewListSize() {
        return newArticleList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldArticleList.get(oldItemPosition).getTitle() == newArticleList.get(newItemPosition).getTitle();
    }


    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Articles oldArticle = oldArticleList.get(oldItemPosition);
        Articles newArticle = newArticleList.get(newItemPosition);

        return oldArticle.getTitle().equals(newArticle.getTitle());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
