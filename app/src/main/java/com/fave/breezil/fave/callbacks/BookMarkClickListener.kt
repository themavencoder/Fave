package com.fave.breezil.fave.callbacks

import com.fave.breezil.fave.model.BookMark


interface BookMarkClickListener {
    fun showDetails(bookMark: BookMark)
}
