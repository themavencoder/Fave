package com.fave.breezil.fave.model

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Source(
        @SerializedName("name")
        val name: String
) : Parcelable
