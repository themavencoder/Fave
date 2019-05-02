package com.fave.breezil.fave.ui.bottom_sheets

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.fave.breezil.fave.R
import com.fave.breezil.fave.databinding.FragmentActionBottomSheetBinding
import com.fave.breezil.fave.model.Articles
import com.fave.breezil.fave.model.BookMark
import com.fave.breezil.fave.ui.main.WebActivity
import com.fave.breezil.fave.ui.main.viewmodel.BookMarkViewModel
import com.fave.breezil.fave.utils.Constant.Companion.ARTICLE
import com.fave.breezil.fave.utils.Constant.Companion.ARTICLE_TITLE
import com.fave.breezil.fave.utils.Constant.Companion.ARTICLE_URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 */
class ActionBottomSheetFragment : BottomSheetDialogFragment() {


    lateinit var binding: FragmentActionBottomSheetBinding
    private var mContext: Context? = null

    private val article: Articles?
        get() = if (arguments!!.getParcelable<Articles>(ARTICLE) != null) {
            arguments!!.getParcelable(ARTICLE)
        } else {
            null
        }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_action_bottom_sheet, container, false)
        this.mContext = activity

        updateUi(this.article!!)
        return binding.root
    }

    private fun updateUi(article: Articles) {
        binding.fullArticle.setOnClickListener { startWeb(article) }
        binding.shareArticle.setOnClickListener { startSharing(article) }
        binding.bookMarkArticle.setOnClickListener {

            startbookMark(article)

//            Glide.with(this@ActionBottomSheetFragment.mContext!!)
//                    .load(article.urlToImage)
//                    .listener(object : RequestListener<Drawable> {
//                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
//                            return true
//                        }
//
//                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
//
//                            startbookMark(article)
//                            Toast.makeText(this@ActionBottomSheetFragment.mContext, R.string.saved_bookmark, Toast.LENGTH_SHORT).show()
//                            return true
//                        }
//                    }).submit()
//            dismiss()
        }
    }


    private fun startSharing(articles: Articles) {
        val activity = activity
        if (activity != null && isAdded) {

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = getString(R.string.text_slash_plain)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, articles.title)
            shareIntent.putExtra(Intent.EXTRA_TEXT, articles.url)
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_link)))

        }
        dismiss()

    }


    private fun startbookMark(article: Articles){
        if (activity != null && isAdded) {

            val title = article.title
            val description = article.description
            val url = article.url
            val urlToImage = article.urlToImage
            val publishedAt = article.publishedAt
            val source = article.source!!.name

            val bookMarkViewModel = ViewModelProviders.of(this)
                    .get(BookMarkViewModel::class.java)
            if(title != null && description != null && url != null && urlToImage != null
                    && publishedAt !=null
            ){
                val bookMark = BookMark( title, description,
                        url, urlToImage, publishedAt,source)
                bookMarkViewModel.insert(bookMark)
                Toast.makeText(this@ActionBottomSheetFragment.mContext, R.string.saved_bookmark, Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(this@ActionBottomSheetFragment.mContext, getString(R.string.ops_cant_save_bookmark_at_this_time), Toast.LENGTH_SHORT).show()

            }

        }
        dismiss()
    }

    private fun startWeb(articles: Articles) {
        val activity = activity
        if (activity != null && isAdded)  {

            val webIntent = Intent(context, WebActivity::class.java)
            webIntent.putExtra(ARTICLE_TITLE, articles.title)
            webIntent.putExtra(ARTICLE_URL, articles.url)
            startActivity(webIntent)
        }
        dismiss()
    }


    companion object {
        fun getArticles(article: Articles): ActionBottomSheetFragment {
            val fragment = ActionBottomSheetFragment()
            val args = Bundle()
            args.putParcelable(ARTICLE, article)
            fragment.arguments = args
            return fragment

        }
    }



}// Required empty public constructor
