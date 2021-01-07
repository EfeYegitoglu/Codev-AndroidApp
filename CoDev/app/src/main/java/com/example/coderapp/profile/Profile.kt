package com.example.coderapp.profile

import com.example.coderapp.account.User
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Profile(
    @SerializedName("profile_id")
    @Expose
    var profile_id: Int,
    @SerializedName("profile_picture")
    @Expose
    var profile_picture: String,
    @SerializedName("profile_picture_url")
    @Expose
    var profile_picture_url: String,
    @SerializedName("profile_name")
    @Expose
    var profile_name: String,
    @SerializedName("profile_info")
    @Expose
    var profile_info: String,
    @SerializedName("profile_website")
    @Expose
    var profile_website: String,
    @SerializedName("user")
    @Expose
    var user: Int

) : Serializable {
}