package com.example.breezil.fave.ui.bottom_sheets


import android.app.AlertDialog
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

import com.example.breezil.fave.R
import com.example.breezil.fave.databinding.FragmentBookmarkBottomSheetBinding
import com.example.breezil.fave.model.BookMark
import com.example.breezil.fave.ui.main.WebActivity
import com.example.breezil.fave.ui.main.viewmodel.BookMarkViewModel

import com.example.breezil.fave.utils.Constant.Companion.ARTICLE_TITLE
import com.example.breezil.fave.utils.Constant.Companion.ARTICLE_URL
import com.example.breezil.fave.utils.Constant.Companion.BOOKMARK

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 */
class BookmarkBottomSheetFragment : BottomSheetDialogFragment() {

    private var mContext: Context? = null
    lateinit var bookMarkViewModel: BookMarkViewModel
    lateinit var binding: FragmentBookmarkBottomSheetBinding


    private val bookmarked: BookMark?
        get() = if (arguments!!.getParcelable<BookMark>(BOOKMARK) != null) {
            arguments!!.getParcelable(BOOKMARK)
        } else {
            null
        }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark_bottom_sheet, container, false)
        bookMarkViewModel = ViewModelProviders.of(this).get(BookMarkViewModel::class.java)
        this.mContext = activity
        updateUi(this.bookmarked!!)
        return binding.root
    }

    private fun updateUi(bookmarked: BookMark) {
        binding.fullArticle.setOnClickListener { startWeb(bookmarked) }
        binding.shareArticle.setOnClickListener { startSharing(bookmarked) }
        binding.deleteBookmark.setOnClickListener { showDeleteDialog(bookmarked) }
    }

    private fun showDeleteDialog(bookMark: BookMark) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setMessage(R.string.Are_you_sure_you_want_to_delete_this_bookmark).setPositiveButton(R.string.yes) { _, _ ->
            bookMarkViewModel.delete(bookMark)

            Toast.makeText(mContext,
                    resources.getString(R.string.bookmark_deleted), Toast.LENGTH_SHORT).show()

            dismiss()
        }
                .setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.setTitle(getString(R.string.delete_bookmark))
        alertDialog.show()

    }


    private fun startSharing(bookMark: BookMark) {
        val activity = activity
        if (activity != null && isAdded) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, bookMark.title)
            shareIntent.putExtra(Intent.EXTRA_TEXT, bookMark.url)
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_link)))

        }
        dismiss()

    }

    private fun startWeb(bookMark: BookMark) {
        val activity = activity
        if (activity != null && isAdded)  {
            val webIntent = Intent(context, WebActivity::class.java)
            webIntent.putExtra(ARTICLE_TITLE, bookMark.title)
            webIntent.putExtra(ARTICLE_URL, bookMark.url)
            startActivity(webIntent)
        }
        dismiss()
    }

    companion object {

        fun getBookmark(bookMark: BookMark): BookmarkBottomSheetFragment {
            val bookmarkBottomSheetFragment = BookmarkBottomSheetFragment()
            val args = Bundle()
            args.putParcelable(BOOKMARK, bookMark)
            bookmarkBottomSheetFragment.arguments = args
            return bookmarkBottomSheetFragment
        }
    }


}// Required empty public constructor
