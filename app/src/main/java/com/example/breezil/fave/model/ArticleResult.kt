package com.example.breezil.fave.model

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

class ArticleResult : Parcelable {

    @SerializedName("status")
    var status: String? = null
    @SerializedName("totalResults")
    var totalResults: Int = 0
    @SerializedName("message")
    var message: String? = null
    @SerializedName("articles")
    var articles: List<Articles>? = null

    constructor() {}

    protected constructor(`in`: Parcel) {
        status = `in`.readString()
        totalResults = `in`.readInt()
        message = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(status)
        dest.writeInt(totalResults)
        dest.writeString(message)
    }



    companion object CREATOR : Parcelable.Creator<ArticleResult> {
        override fun createFromParcel(parcel: Parcel): ArticleResult {
            return ArticleResult(parcel)
        }

        override fun newArray(size: Int): Array<ArticleResult?> {
            return arrayOfNulls(size)
        }
    }
}
