package com.example.breezil.fave.ui.adapter

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.breezil.fave.R
import com.example.breezil.fave.callbacks.ArticleLongClickListener
import com.example.breezil.fave.callbacks.ArticleClickListener
import com.example.breezil.fave.databinding.ArticleItemBinding
import com.example.breezil.fave.databinding.ItemNetworkStateBinding
import com.example.breezil.fave.model.Articles
import com.example.breezil.fave.repository.NetworkState
import com.example.breezil.fave.utils.helpers.HtmlTagHandler


class ArticleRecyclerViewAdapter(internal var context: Context, private val articleClickListener: ArticleClickListener,
                                 private val articleLongClickListener: ArticleLongClickListener) : PagedListAdapter<Articles, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    internal lateinit var circularProgressDrawable: CircularProgressDrawable
    private var networkState: NetworkState? = null

    internal lateinit var binding: ArticleItemBinding
    private var networkStateBinding: ItemNetworkStateBinding? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        if (viewType == TYPE_PROGRESS) {
            networkStateBinding = ItemNetworkStateBinding.inflate(layoutInflater, parent, false)
            return NetworkStateItemViewHolder(networkStateBinding!!)
        } else {
            binding = ArticleItemBinding.inflate(layoutInflater, parent, false)

            return ArticleHolder(binding)
        }

    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {


        if (viewHolder is ArticleHolder) {
            val article = getItem(position)

            viewHolder.bind(article, articleClickListener, articleLongClickListener)
        } else {
            (viewHolder as NetworkStateItemViewHolder).bindView(networkState)
        }

    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            TYPE_PROGRESS
        } else {
            TYPE_ITEM
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val previousExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val newExtraRow = hasExtraRow()
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }


    fun getArticlesAt(position: Int): Articles? {
        return getItem(position)
    }

    internal inner class ArticleHolder(var binding: ArticleItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(articles: Articles?, articleClickListener: ArticleClickListener, articleLongClickListener: ArticleLongClickListener) {
            itemView.setOnClickListener { articleClickListener.showDetails(articles!!) }
            itemView.setOnLongClickListener {
                articleLongClickListener.doSomething(articles!!)
                true
            }

//            binding.articleTitle.text = articles!!.title

            binding.articleTitle.text = Html.fromHtml(articles!!.title, null, HtmlTagHandler())


            circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 12f
            circularProgressDrawable.centerRadius = 60f
            circularProgressDrawable.setColorSchemeColors(R.color.colorAccent, R.color.colorPrimary,
                    R.color.colorblue, R.color.hotPink)
            circularProgressDrawable.start()

            Glide.with(context)
                    .load(articles.urlToImage)
                    .apply(RequestOptions()
                            .placeholder(circularProgressDrawable)
                            .error(R.drawable.placeholder))
                    .into(binding.articleImage)

            if (articles.source != null) {
                binding.sourcesText.text = articles.source!!.name
            }


        }
    }

    inner class NetworkStateItemViewHolder internal constructor(private val binding: ItemNetworkStateBinding) : RecyclerView.ViewHolder(binding.root) {

        internal fun bindView(networkState: NetworkState?) {
            if (networkState != null && networkState.status === NetworkState.Status.RUNNING) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }

            if (networkState != null && networkState.status === NetworkState.Status.FAILED) {
                binding.errorMsg.visibility = View.VISIBLE
            } else {
                binding.errorMsg.visibility = View.GONE
            }
        }
    }

    companion object {

        private const val TYPE_PROGRESS = 0
        private const val TYPE_ITEM = 1


        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Articles>() {
            override fun areItemsTheSame(oldItem: Articles, newItem: Articles): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: Articles, newItem: Articles): Boolean {
                return (oldItem.title == newItem.title
                        && oldItem.description == newItem.description
                        && oldItem.publishedAt == newItem.publishedAt)
            }
        }
    }
}
