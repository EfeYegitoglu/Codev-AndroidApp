package com.example.coderapp.category

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CategoryResponse(@SerializedName("categories")
@Expose
var categories:List<Category>,
@SerializedName("success")
@Expose
var success:Int) {
}