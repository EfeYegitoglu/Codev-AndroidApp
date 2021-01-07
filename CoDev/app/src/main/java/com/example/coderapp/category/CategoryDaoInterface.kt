package com.example.coderapp.category

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface CategoryDaoInterface {

    @GET("coderapp/all_category.php")
    fun allCategories():Call<CategoryResponse>

    @POST("coderapp/search_category.php")
    @FormUrlEncoded
    fun searchCategory(@Field("category_name") category_name:String):Call<CategoryResponse>
}