package com.fave.breezil.fave.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import com.google.gson.annotations.SerializedName


@Parcelize
@Entity(tableName = "bookmark_table")
data class BookMark(

        @SerializedName("title")
        var title: String? = "",
        @SerializedName("description")
        var description: String? = "",
        @SerializedName("url")
        var url: String? = "",
        @SerializedName("urlToImage")
        var urlToImage: String? ="",
        @SerializedName("publishedAt")
        var publishedAt: String? = "",

        @SerializedName("source")
        var source: String?= ""



) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}
