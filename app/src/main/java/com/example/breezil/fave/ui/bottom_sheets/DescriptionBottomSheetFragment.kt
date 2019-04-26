package com.example.breezil.fave.ui.bottom_sheets

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.support.v4.widget.CircularProgressDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.breezil.fave.R
import com.example.breezil.fave.databinding.FragmentDescriptionBottomSheetBinding
import com.example.breezil.fave.model.Articles

import com.example.breezil.fave.utils.Constant.Companion.ARTICLE

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 */
class DescriptionBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentDescriptionBottomSheetBinding
    private var mContext: Context? = null
    lateinit var circularProgressDrawable: CircularProgressDrawable



    private val article: Articles?
        get() = if (arguments!!.getParcelable<Articles>(ARTICLE) != null) {
            arguments!!.getParcelable(ARTICLE)
        } else {
            null
        }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_description_bottom_sheet, container, false)
        this.mContext = activity
        updateUi(article!!)
        return binding.root
    }

    private fun updateUi(article: Articles) {
        circularProgressDrawable = CircularProgressDrawable(context!!)
        circularProgressDrawable.strokeWidth = 10f
        circularProgressDrawable.centerRadius = 40f
        circularProgressDrawable.setColorSchemeColors(R.color.colorAccent, R.color.colorPrimary,
                R.color.colorblue, R.color.hotPink)
        circularProgressDrawable.start()
        Glide.with(mContext!!)
                .load(article.urlToImage)
                .apply(RequestOptions()


                        .placeholder(circularProgressDrawable)
                        .error(R.drawable.placeholder))
                .into(binding.articleImage)
        binding.articleDescriptions.text = article.description
        binding.articleTitle.text = article.title
        binding.articleSource.text = article.source!!.name

    }

    companion object {
        fun getArticles(article: Articles): DescriptionBottomSheetFragment {
            val fragment = DescriptionBottomSheetFragment()
            val args = Bundle()
            args.putParcelable(ARTICLE, article)
            fragment.arguments = args
            return fragment

        }
    }

}// Required empty public constructor
