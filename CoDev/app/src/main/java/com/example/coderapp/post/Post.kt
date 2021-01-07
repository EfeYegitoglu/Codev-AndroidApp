package com.example.coderapp.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Post(
    @SerializedName("post_id")
    @Expose
    var post_id: Int,
    @SerializedName("user_id")
    @Expose
    var user_id: Int,
    @SerializedName("post_picture_one")
    @Expose
    var post_picture_one: String,
    @SerializedName("post_picture_url_one")
    @Expose
    var post_picture_url_one: String,
    @SerializedName("post_picture_two")
    @Expose
    var post_picture_two: String,
    @SerializedName("post_picture_url_two")
    @Expose
    var post_picture_url_two: String,
    @SerializedName("post_picture_three")
    @Expose
    var post_picture_three: String,
    @SerializedName("post_picture_url_three")
    @Expose
    var post_picture_url_three: String,
    @SerializedName("post_title")
    @Expose
    var post_title: String,
    @SerializedName("post_explanation")
    @Expose
    var post_explanation: String,
    @SerializedName("post_category")
    @Expose
    var post_category: String
) : Serializable {
}