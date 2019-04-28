package com.example.breezil.fave.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "article_table")
data class Articles(
        @SerializedName("author")
        val author: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("urlToImage")
        val urlToImage: String,
        @SerializedName("publishedAt")
        val publishedAt: String,

        @SerializedName("source")
        @Embedded
        val source: Source,

        @SerializedName("content")
        val content: String
): Parcelable {

        @IgnoredOnParcel
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0
}
