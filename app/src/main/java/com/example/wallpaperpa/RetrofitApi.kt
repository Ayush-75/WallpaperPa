package com.example.wallpaperpa

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitApi {

    @Headers("Authorization:kErlhisyG9fhF8fVHTMiS0LukV2nno6I0eVcbAw9h3t2XblADQnAT5Cq")
    @GET("curated?per_page=30&page=1")
    fun getWallpapers():Call<WallpaperModel>?

    @Headers("Authorization:kErlhisyG9fhF8fVHTMiS0LukV2nno6I0eVcbAw9h3t2XblADQnAT5Cq")
    @GET("search?")
    fun getWallpaperByCategory(
        @Query("query") category:String,
        @Query("per_page") per_page:Int,
        @Query("page")page:Int
    ):Call<WallpaperModel>?

}