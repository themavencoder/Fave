package com.example.breezil.fave.callbacks

import com.example.breezil.fave.model.BookMark

interface BookMarkClickListener {
    fun showDetails(bookMark: BookMark)
}
