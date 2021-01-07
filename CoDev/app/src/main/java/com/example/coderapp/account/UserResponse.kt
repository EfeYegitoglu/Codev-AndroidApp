package com.example.coderapp.account

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserResponse(
    @SerializedName("users")
    @Expose
    var users: List<User>,
    @SerializedName("success")
    @Expose
    var success: Int
):Serializable {
}