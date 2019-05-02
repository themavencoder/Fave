package com.fave.breezil.fave.ui.adapter

import android.support.v7.util.DiffUtil

import com.fave.breezil.fave.model.Articles

class DiffUtils(private val oldArticleList: List<Articles>, private val newArticleList: List<Articles>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldArticleList.size
    }

    override fun getNewListSize(): Int {
        return newArticleList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldArticleList[oldItemPosition].title === newArticleList[newItemPosition].title
    }


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldArticle = oldArticleList[oldItemPosition]
        val newArticle = newArticleList[newItemPosition]

        return oldArticle.title == newArticle.title
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}
