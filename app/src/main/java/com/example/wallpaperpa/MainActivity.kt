package com.example.wallpaperpa

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), CategoryRVAdapter.CategoryClickInterface {

    lateinit var wallpaperRV: RecyclerView
    lateinit var categoryRV: RecyclerView
    lateinit var categoryRVAdapter: CategoryRVAdapter
    lateinit var wallpaperRVAdapter: WallpaperRVAdapter
    lateinit var loadingPB: ProgressBar
    lateinit var searchEdit: EditText
    lateinit var searchIV: ImageView
    lateinit var wallpaperList: ArrayList<String>
    lateinit var categoryList: ArrayList<CategoryModel>
    lateinit var retrofitApi: RetrofitApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.statusBarColor = getColor(R.color.black_shade)
        wallpaperRV = findViewById(R.id.RVWallpaper)
        categoryRV = findViewById(R.id.RVCategory)
        loadingPB = findViewById(R.id.PBLoading)
        searchEdit = findViewById(R.id.search_bar)
        searchIV = findViewById(R.id.idIVsearch)

        wallpaperList = ArrayList<String>()
        categoryList = ArrayList<CategoryModel>()

        getCategories()
        categoryRVAdapter = CategoryRVAdapter(categoryList, this, this)
        categoryRV.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        categoryRV.adapter = categoryRVAdapter
        categoryRVAdapter.notifyDataSetChanged()



        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofitApi = retrofit.create(RetrofitApi::class.java)

        searchEdit.setOnClickListener {
            if (searchEdit.text.toString().isNotBlank()) {
                getWallpaperByCategory(retrofitApi, searchEdit.text.toString())
            }
        }
        getWallpaperByCategory(retrofitApi, "")

    }

    private fun getCategories() {

        categoryList.add( CategoryModel(
            "Cars",
            "https://images.pexels.com/photos/164634/pexels-photo-164634.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"
        ))
        categoryList.add(CategoryModel(
            "Nature",
            "https://images.pexels.com/photos/3225517/pexels-photo-3225517.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"
        ))
        categoryList.add(CategoryModel(
            "Architecture",
            "https://images.pexels.com/photos/262367/pexels-photo-262367.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"
        ))
        categoryList.add(CategoryModel(
            "Anime",
            "https://images.pexels.com/photos/1921336/pexels-photo-1921336.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"
        ))
        categoryList.add( CategoryModel(
            "Abstract",
            "https://images.pexels.com/photos/2693208/pexels-photo-2693208.png?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"
        ))
        categoryList.add(CategoryModel(
            "Marvel",
            "https://images.pexels.com/photos/2854693/pexels-photo-2854693.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"
        ))
        categoryList.add(CategoryModel(
            "Arts",
            "https://images.pexels.com/photos/1839919/pexels-photo-1839919.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"
        ))
        categoryList.add(CategoryModel(
            "Music",
            "https://images.pexels.com/photos/1105666/pexels-photo-1105666.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"
        ))
        categoryList.add(CategoryModel(
            "Flowers",
            "https://images.pexels.com/photos/56866/garden-rose-red-pink-56866.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"
        ))

    }

    private fun getWallpaperByCategory(retrofitApi: RetrofitApi, category: String) {
        var call: Call<WallpaperModel>? = null
        if (category.isNotEmpty()) {
            call = retrofitApi.getWallpaperByCategory(category, 30, 1)
        } else {
            call = retrofitApi.getWallpapers()
        }

        wallpaperList = ArrayList<String>()
        loadingPB.visibility = View.VISIBLE

        call!!.enqueue(object : Callback<WallpaperModel?> {
            override fun onResponse(
                call: Call<WallpaperModel?>,
                response: Response<WallpaperModel?>
            ) {
                if (response.isSuccessful) {
                    val dt = response.body()
                    loadingPB.visibility = View.GONE
                    val photosList: ArrayList<Photos> = dt!!.photos

                    if (photosList.isNotEmpty()) {
                        for (i in 0 until photosList.size) {
                            val photoObj = photosList.get(i)
                            val imgUrl: String = photoObj.src.portrait
                            wallpaperList.add(imgUrl)
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "No wallpaper found", Toast.LENGTH_SHORT)
                            .show()
                    }

                    wallpaperRV.layoutManager = GridLayoutManager(applicationContext,2)
                    wallpaperRVAdapter = WallpaperRVAdapter(wallpaperList, applicationContext)
                    wallpaperRV.adapter = wallpaperRVAdapter
                    wallpaperRVAdapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@MainActivity,"Fail to get response", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WallpaperModel?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "fail to fetch", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCategoryClick(position: Int) {
        val category: String = categoryList.get(position).categoryName
        getWallpaperByCategory(retrofitApi, category)
    }
}