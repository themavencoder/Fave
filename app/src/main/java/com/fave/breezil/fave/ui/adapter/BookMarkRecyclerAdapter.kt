package com.fave.breezil.fave.ui.adapter

import android.content.Context
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.support.v7.recyclerview.extensions.ListAdapter
import android.view.ViewGroup

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fave.breezil.fave.R
import com.fave.breezil.fave.databinding.ArticleItemBinding
import android.text.Html
import com.fave.breezil.fave.callbacks.BookMarkClickListener
import com.fave.breezil.fave.callbacks.BookMarkLongClickListener
import com.fave.breezil.fave.model.BookMark
import com.fave.breezil.fave.utils.helpers.HtmlTagHandler


class BookMarkRecyclerAdapter(internal var context: Context, private val listener: BookMarkClickListener,
                              private val longClickListener: BookMarkLongClickListener) :
        ListAdapter<BookMark, BookMarkRecyclerAdapter.BookMarkHolder>(DIFF_CALLBACK) {
    internal lateinit var binding: ArticleItemBinding
    internal lateinit var circularProgressDrawable: CircularProgressDrawable

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ArticleItemBinding.inflate(layoutInflater, parent, false)
        return BookMarkHolder(binding)
    }

    override fun onBindViewHolder(holder: BookMarkHolder, position: Int) {
        val bookMark = getItem(position)
        holder.bind(bookMark, listener, longClickListener)
    }

    fun getBookMarkAt(position: Int): BookMark {
        return getItem(position)
    }

    inner class BookMarkHolder(var binding: ArticleItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookMark: BookMark, listener: BookMarkClickListener, longClickListener: BookMarkLongClickListener) {
            itemView.setOnClickListener { listener.showDetails(bookMark) }
            itemView.setOnLongClickListener {
                longClickListener.doSomething(bookMark)
                true
            }

            circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 12f
            circularProgressDrawable.centerRadius = 60f
            circularProgressDrawable.setColorSchemeColors(R.color.colorAccent, R.color.colorPrimary,
                    R.color.colorblue,R.color.hotPink)
            circularProgressDrawable.start()
            Glide.with(context)
                    .load(bookMark.urlToImage)
                    .apply(RequestOptions()
                            .placeholder(circularProgressDrawable)
                            .error(R.drawable.placeholder))
                    .into(binding.articleImage)


//            binding.articleTitle.text = bookMark.title
            binding.sourcesText.text = bookMark.source
            binding.articleTitle.text = Html.fromHtml(bookMark.title, null, HtmlTagHandler())
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookMark>() {
            override fun areItemsTheSame(oldItem: BookMark, newItem: BookMark): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: BookMark, newItem: BookMark): Boolean {
                return (oldItem.title == newItem.title
                        && oldItem.description == newItem.description
                        && oldItem.publishedAt == newItem.publishedAt)
            }
        }
    }
}
