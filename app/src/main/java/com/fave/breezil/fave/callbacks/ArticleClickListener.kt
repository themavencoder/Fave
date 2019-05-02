package com.fave.breezil.fave.callbacks

import com.fave.breezil.fave.model.Articles

interface ArticleClickListener {
    fun showDetails(article: Articles)
}
