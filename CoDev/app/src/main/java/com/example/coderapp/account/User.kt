package com.example.coderapp.account

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("user_id")
    @Expose
    var user_id: Int,
    @SerializedName("user_mail")
    @Expose
    var user_mail: String,
    @SerializedName("user_password")
    @Expose
    var user_password: String,
    @SerializedName("verification_code")
    @Expose
    var verification_code: String,
    @SerializedName("account_state")
    @Expose
    var account_state: String
) : Serializable {
}