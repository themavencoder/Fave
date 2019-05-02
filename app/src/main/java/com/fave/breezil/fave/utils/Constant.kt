package com.fave.breezil.fave.utils

import com.fave.breezil.fave.BuildConfig

import java.util.ArrayList
import java.util.LinkedList

class Constant {
    companion object {
        const val API_KEY = BuildConfig.API_KEY
        const val BASE_URL = BuildConfig.BASE_URL
        const val ARTICLE = "article"
        const val ARTICLE_TITLE = "article_title"
        const val ARTICLE_URL = "article_url"
        const val BOOKMARK = "bookmark"
        const val SOURCENAME = "source"
        const val SEARCH_HINT = "search eg bitcoin, google, politics .."
        const val ARTICLE_LIST = "article_list"
        const val FRAGMENT_TYPE = "fragment_type"
        const val PREFERED_FRAGEMENT = "Preferred"
        const val HEADLINE_FRAGMENT = "Top Stories"
        const val BOOKMARK_FRAGMENT = "BookMarks"
        const val BOOKMARK_ID = "BookMarksID"
        const val SEARCH_RESULT = "Search Result"
        const val ABOUT = "About"
        const val DEFAULT_SOURCE = "bbc-news,axios,cnn,daily-mail,espn,google-news,the-new-york-times"
        const val FAVE_DB = "favnew.db"


        const val ZERO = 0
        const val ONE = 1
        const val TWO = 2
        const val THREE = 3
        const val FIVE_HUNDRED = 500
        const val ONE_THOUSAND = 2000
        const val TWO_THOUSAND = 1000
        const val FOUR = 4
        const val FIVE = 5
        const val TEN = 10
        const val ONE_HUNDRED = 100
        const val DELAY = 800


        fun pickNRandom(lst: ArrayList<String>, n: Int): List<String> {
            val copy = LinkedList(lst)
            copy.shuffle()
            return copy.subList(0, n)
        }
    }


}

