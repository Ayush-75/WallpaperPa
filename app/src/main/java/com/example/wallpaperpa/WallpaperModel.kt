package com.example.wallpaperpa

data class WallpaperModel(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: ArrayList<Photos>,
    val total_results: Int
)