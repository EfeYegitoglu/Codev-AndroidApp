package com.example.coderapp.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Comment(
    @SerializedName("comment_id")
    @Expose
    var commentId: Int,
    @SerializedName("post_id")
    @Expose
    var postId: Int,
    @SerializedName("comment_text")
    @Expose
    var commentText: String,
    @SerializedName("user_id")
    @Expose
    var userId: Int
):Serializable {
}