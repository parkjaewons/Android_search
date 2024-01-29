package com.example.searchapi.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Document(
    @SerializedName("display_sitename")
    val siteName : String,
    val collection : String,
    val image_url : String,
    val thumbnail_url : String,
    val datetime : Date?
) : Parcelable
