package com.example.breezil.fave.ui.main.fragment


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

import com.example.breezil.fave.callbacks.BookMarkLongClickListener
import com.example.breezil.fave.databinding.FragmentBookMarksBinding
import com.example.breezil.fave.R
import com.example.breezil.fave.callbacks.BookMarkClickListener
import com.example.breezil.fave.model.BookMark
import com.example.breezil.fave.ui.adapter.BookMarkRecyclerAdapter
import com.example.breezil.fave.ui.bottom_sheets.BookMarkDescriptionFragment
import com.example.breezil.fave.ui.bottom_sheets.BookmarkBottomSheetFragment
import com.example.breezil.fave.ui.main.viewmodel.BookMarkViewModel

import dagger.android.support.AndroidSupportInjection

/**
 * A simple [Fragment] subclass.
 */
class BookMarksFragment : Fragment() {

    lateinit var adapter: BookMarkRecyclerAdapter
    lateinit var bookMarkViewModel: BookMarkViewModel

    lateinit var binding: FragmentBookMarksBinding


    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
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

        val bookMarkClickListener = object : BookMarkClickListener{
            override fun showDetails(bookMark: BookMark) {
                val bookMarkDescriptionFragment = BookMarkDescriptionFragment.getBookMarked(bookMark)
                bookMarkDescriptionFragment.show(fragmentManager!!, "show")
            }
        }
        val bookMarkLongClickListener = object : BookMarkLongClickListener{
            override fun doSomething(bookMark: BookMark) {
                val bookmarkBottomSheetFragment = BookmarkBottomSheetFragment.getBookmark(bookMark)
                bookmarkBottomSheetFragment.show(fragmentManager!!, "show")
            }
        }

        adapter = BookMarkRecyclerAdapter(context!!, bookMarkClickListener, bookMarkLongClickListener)
        binding.bookmarkList.adapter = adapter


    }

    @SuppressLint("RestrictedApi")
    private fun setUpViewModel() {
        bookMarkViewModel = ViewModelProviders.of(this).get(BookMarkViewModel::class.java)
        bookMarkViewModel.bookmarkList.observe(this, Observer{ bookMarks ->
            if(!bookMarks!!.isEmpty()){
                adapter.submitList(bookMarks)
                binding.deleteAll.visibility = View.VISIBLE
            }
        })

    }


    private fun showDeleteAllDialog() {

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setMessage("Are you sure, you want to delete all bookmarks?").setPositiveButton("Yes") { dialog, _ ->
            deleteAll()
            dialog.dismiss()

            Toast.makeText(activity, "BookMark list emptied", Toast.LENGTH_SHORT).show()
        }
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.setTitle("Delete All")
        alertDialog.show()

    }

    private fun deleteAll() {
        bookMarkViewModel.deleteAll()
    }
}
