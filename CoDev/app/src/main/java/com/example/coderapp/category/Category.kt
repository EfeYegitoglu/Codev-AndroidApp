package com.example.coderapp.category

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Category(@SerializedName("category_id")
@Expose
var category_id:Int,
@SerializedName("category_name")
@Expose
var category_name: String,
@SerializedName("category_picture")
@Expose
var category_picture:String,
@SerializedName("category_post_count")
@Expose
var category_post_count:Int):Serializable {
}