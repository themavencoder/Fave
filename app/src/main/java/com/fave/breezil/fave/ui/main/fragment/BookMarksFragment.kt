package com.fave.breezil.fave.ui.main.fragment


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fave.breezil.fave.databinding.FragmentBookMarksBinding
import com.fave.breezil.fave.R
import com.fave.breezil.fave.callbacks.BookMarkClickListener
import com.fave.breezil.fave.callbacks.BookMarkLongClickListener
import com.fave.breezil.fave.model.BookMark
import com.fave.breezil.fave.ui.adapter.BookMarkRecyclerAdapter
import com.fave.breezil.fave.ui.bottom_sheets.BookMarkDescriptionFragment
import com.fave.breezil.fave.ui.bottom_sheets.BookmarkBottomSheetFragment
import com.fave.breezil.fave.ui.main.viewmodel.BookMarkViewModel

import dagger.android.support.AndroidSupportInjection





/**
 * A simple [Fragment] subclass.
 */
class BookMarksFragment : Fragment() {

    interface RefreshBookmark {
        fun getRefresh(refresh: Boolean)
    }

    lateinit var refreshBookmark : RefreshBookmark
    lateinit var adapter: BookMarkRecyclerAdapter
    lateinit var bookMarkViewModel: BookMarkViewModel

    lateinit var binding: FragmentBookMarksBinding


    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        try {
            this.refreshBookmark = context as RefreshBookmark
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement UriListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_marks, container,
                false)
        binding.bookmarkList.setHasFixedSize(true)


        binding.deleteAll.setOnClickListener { showDeleteAllDialog() }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpAdapter()
        setUpViewModel()

    }

    private fun setUpAdapter() {

        val bookMarkClickListener = object : BookMarkClickListener {
            override fun showDetails(bookMark: BookMark) {
                val bookMarkDescriptionFragment = BookMarkDescriptionFragment.getBookMarked(bookMark)
                bookMarkDescriptionFragment.show(fragmentManager!!, getString(R.string.show))
            }
        }
        val bookMarkLongClickListener = object : BookMarkLongClickListener {
            override fun doSomething(bookMark: BookMark) {
                val bookmarkBottomSheetFragment = BookmarkBottomSheetFragment.getBookmark(bookMark)
                bookmarkBottomSheetFragment.show(fragmentManager!!, getString(R.string.show))
            }
        }

        adapter = BookMarkRecyclerAdapter(context!!, bookMarkClickListener, bookMarkLongClickListener)
        binding.bookmarkList.adapter = adapter


    }

    @SuppressLint("RestrictedApi")
    private fun setUpViewModel() {
        bookMarkViewModel = ViewModelProviders.of(this).get(BookMarkViewModel::class.java)
        bookMarkViewModel.bookmarkList.observe(this, Observer { bookMarks ->
            if (!bookMarks!!.isEmpty()) {
                adapter.submitList(bookMarks)
                binding.deleteAll.visibility = View.VISIBLE
            }
        })

    }


    private fun showDeleteAllDialog() {

        val builder = AlertDialog.Builder(context, R.style.MyDialogTheme)
        builder.setCancelable(false)
        builder.setMessage(getString(R.string.are_you_sure_you_want_to_delete_all_bookmarks))
                .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            deleteAll()
            dialog.dismiss()
//            refresh()
            adapter.notifyDataSetChanged()
            Toast.makeText(activity, getString(R.string.bookmark_list_emptied), Toast.LENGTH_SHORT).show()
        }
                .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.setTitle(getString(R.string.delete_all))
        alertDialog.show()

    }


    private fun deleteAll() {
        bookMarkViewModel.deleteAll()
    }

    private fun refresh(){
        val ft = fragmentManager!!.beginTransaction()

        ft.detach(this@BookMarksFragment).attach(this@BookMarksFragment).commit()
    }

}
