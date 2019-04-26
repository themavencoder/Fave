package com.example.breezil.fave.model

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName


class Source : Parcelable {


    @SerializedName("name")
    var name: String? = null

    var articleId: Int = 0

    constructor() {}

    protected constructor(`in`: Parcel) {
        name = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
    }



    companion object CREATOR : Parcelable.Creator<Source> {
        override fun createFromParcel(parcel: Parcel): Source {
            return Source(parcel)
        }

        override fun newArray(size: Int): Array<Source?> {
            return arrayOfNulls(size)
        }
    }
}
