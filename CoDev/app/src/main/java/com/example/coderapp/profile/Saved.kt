package com.example.coderapp.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Saved(
    @SerializedName("saved_id")
    @Expose
    var saved_id:Int,
    @SerializedName("post_id")
    @Expose
    var post_id:Int,
    @SerializedName("user_id")
    @Expose
    var user_id:Int
):Serializable {
}