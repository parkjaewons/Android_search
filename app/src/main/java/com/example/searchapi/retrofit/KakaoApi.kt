package com.example.searchapi.retrofit

import com.example.searchapi.data.ImageSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val AUTH_HEADER = "KakaoAK 2329abeaffea6dfd3626b67e6b5044ba"
interface KakaoApi {
    @GET("v2/search/image")
    suspend fun searchImage(
        @Header("Authorization") apiKey: String = AUTH_HEADER,
        @Query("query") query : String,
        @Query("sort") sort : String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<ImageSearchResponse>
}