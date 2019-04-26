package com.example.breezil.fave.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

@Entity(tableName = "article_table")
class Articles : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @SerializedName("author")
    var author: String? = null
    @SerializedName("title")
    var title: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("url")
    var url: String? = null
    @SerializedName("urlToImage")
    var urlToImage: String? = null
    @SerializedName("publishedAt")
    var publishedAt: String? = null

    @Ignore
    @SerializedName("source")
    var source: Source? = null


    @SerializedName("content")
    var content: String? = null

    constructor() {}

    @Ignore
    constructor(id: Int, author: String, title: String, description: String, url: String, urlToImage: String, publishedAt: String, source: Source, content: String) {
        this.id = id
        this.author = author
        this.title = title
        this.description = description
        this.url = url
        this.urlToImage = urlToImage
        this.publishedAt = publishedAt
        this.source = source
        this.content = content
    }

    constructor(author: String, title: String, description: String, url: String, urlToImage: String, publishedAt: String, source: Source, content: String) {

        this.author = author
        this.title = title
        this.description = description
        this.url = url
        this.urlToImage = urlToImage
        this.publishedAt = publishedAt
        this.source = source
        this.content = content
    }

    constructor(`in`: Parcel) {
        id = `in`.readInt()
        author = `in`.readString()
        title = `in`.readString()
        description = `in`.readString()
        url = `in`.readString()
        urlToImage = `in`.readString()
        publishedAt = `in`.readString()
        content = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(author)
        dest.writeString(title)
        dest.writeString(description)
        dest.writeString(url)
        dest.writeString(urlToImage)
        dest.writeString(publishedAt)
        dest.writeString(content)
    }


    companion object CREATOR : Parcelable.Creator<Articles> {
        override fun createFromParcel(parcel: Parcel): Articles {
            return Articles(parcel)
        }

        override fun newArray(size: Int): Array<Articles?> {
            return arrayOfNulls(size)
        }
    }
}
