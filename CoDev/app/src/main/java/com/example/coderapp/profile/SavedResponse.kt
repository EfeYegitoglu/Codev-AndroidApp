package com.example.coderapp.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SavedResponse(
    @SerializedName("saved")
    @Expose
    var saved:List<Saved>?,
    @SerializedName("success")
    @Expose
    var success:Int?,
    @SerializedName("message")
    @Expose
    var message:String?

):Serializable {
}