package com.fave.breezil.fave.ui.adapter

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fave.breezil.fave.R
import com.fave.breezil.fave.callbacks.ArticleClickListener
import com.fave.breezil.fave.databinding.ArticleItemBinding
import com.fave.breezil.fave.callbacks.ArticleLongClickListener
import com.fave.breezil.fave.model.Articles

class SearchRecyclerAdapter(internal var context: Context, private val articlesList: MutableList<Articles>?, private val articleLongClickListener: ArticleLongClickListener,
                            private val articleClickListener: ArticleClickListener) : RecyclerView.Adapter<SearchRecyclerAdapter.SearchHolder>() {
    internal lateinit var binding: ArticleItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ArticleItemBinding.inflate(layoutInflater, parent, false)

        return SearchHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        val articles = articlesList!![position]
        holder.bind(articles, articleClickListener, articleLongClickListener)

    }

    override fun getItemCount(): Int {
        return articlesList?.size ?: 0
    }


    fun updateSearch(list: List<Articles>) {
        val diffUtils = DiffUtils(this.articlesList!!, list)
        val diffResult = DiffUtil.calculateDiff(diffUtils)

        this.articlesList.clear()
        this.articlesList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class SearchHolder(var binding: ArticleItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(articles: Articles, articleClickListener: ArticleClickListener, articleLongClickListener: ArticleLongClickListener) {
            itemView.setOnClickListener { articleClickListener.showDetails(articles) }
            itemView.setOnLongClickListener {
                articleLongClickListener.doSomething(articles)
                true
            }

            binding.articleTitle.text = articles.title

            Glide.with(context)
                    .load(articles.urlToImage)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder))
                    .into(binding.articleImage)

            binding.sourcesText.text = articles.source!!.name

        }
    }


}
