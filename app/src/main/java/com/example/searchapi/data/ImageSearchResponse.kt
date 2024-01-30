package com.example.searchapi.data

import com.google.gson.annotations.SerializedName

data class ImageSearchResponse(
    val meta: MetaData,
    val documents: List<Document>,
)