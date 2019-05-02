package com.fave.breezil.fave.model


import com.google.gson.annotations.SerializedName

data class ArticleResult(
        @SerializedName("status")
        val status: String,
        @SerializedName("totalResults")
        val totalResults: Int = 0,

        @SerializedName("message")
        val message: String,

        @SerializedName("articles")
        val articles: List<Articles>


)
