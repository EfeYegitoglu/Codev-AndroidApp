package com.example.coderapp.profile


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProfileResponse(
    @SerializedName("profiles")
    @Expose
    var profiles: List<Profile>,
    @SerializedName("success")
    @Expose
    var success: Int
) : Serializable {
}