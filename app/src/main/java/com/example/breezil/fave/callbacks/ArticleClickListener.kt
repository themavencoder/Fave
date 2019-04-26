package com.example.breezil.fave.callbacks

import com.example.breezil.fave.model.Articles

interface ArticleClickListener {
    fun showDetails(article: Articles)
}
