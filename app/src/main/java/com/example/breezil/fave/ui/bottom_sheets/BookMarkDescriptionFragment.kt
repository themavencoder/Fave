package com.example.breezil.fave.ui.bottom_sheets


import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.support.v4.widget.CircularProgressDrawable
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.breezil.fave.R
import com.example.breezil.fave.databinding.FragmentBookMarkDescriptionBinding
import com.example.breezil.fave.model.BookMark
import com.example.breezil.fave.utils.Constant.Companion.BOOKMARK
import com.example.breezil.fave.utils.helpers.HtmlTagHandler


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 */
class BookMarkDescriptionFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBookMarkDescriptionBinding
    private var mContext: Context? = null

    lateinit var circularProgressDrawable: CircularProgressDrawable

    private val bookmark: BookMark?
        get() = if (arguments!!.getParcelable<BookMark>(BOOKMARK) != null) {
            arguments!!.getParcelable(BOOKMARK)
        } else {
            null
        }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_mark_description, container, false)

        this.mContext = activity
        updateUi(bookmark!!)
        return binding.root
    }

    private fun updateUi(bookmark: BookMark) {
        circularProgressDrawable = CircularProgressDrawable(context!!)
        circularProgressDrawable.strokeWidth = 10f
        circularProgressDrawable.centerRadius = 40f
        circularProgressDrawable.setColorSchemeColors(R.color.colorAccent, R.color.colorPrimary,
                R.color.colorblue, R.color.hotPink)
        circularProgressDrawable.start()
        Glide.with(mContext!!)
                .load(bookmark.urlToImage)
                .apply(RequestOptions()


                        .placeholder(circularProgressDrawable)
                        .error(R.drawable.placeholder))
                .into(binding.articleImage)
        binding.articleDescriptions.text = bookmark.description
//        binding.articleTitle.text = bookmark.title
        binding.articleTitle.text = Html.fromHtml(bookmark.title, null, HtmlTagHandler())
        binding.articleSource.text = bookmark.source
    }

    companion object {
        fun getBookMarked(bookMark: BookMark): BookMarkDescriptionFragment {
            val fragment = BookMarkDescriptionFragment()
            val args = Bundle()
            args.putParcelable(BOOKMARK, bookMark)
            fragment.arguments = args
            return fragment

        }
    }

}// Required empty public constructor
