package com.fave.breezil.fave.model

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

        @SerializedName("title")
        var title: String?,
        @SerializedName("description")
        var description: String?,
        @SerializedName("url")
        var url: String?,
        @SerializedName("urlToImage")
        var urlToImage: String?,
        @SerializedName("publishedAt")
        var publishedAt: String?,

        @SerializedName("source")
        @Embedded
        var source: Source?


): Parcelable {

        @IgnoredOnParcel
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0
}
