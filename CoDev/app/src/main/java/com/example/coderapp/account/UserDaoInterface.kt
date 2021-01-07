package com.example.coderapp.account

import com.example.coderapp.profile.ProfileResponse
import com.example.coderapp.retrofit.CRUDResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserDaoInterface {

    @POST("coderapp/sign_up.php")
    @FormUrlEncoded
    fun signUp(
        @Field("mailTo") mailTo: String,
        @Field("user_mail") userMail: String,
        @Field("user_password") userPassword: String
    ): Call<CRUDResponse>

    @POST("coderapp/log_in.php")
    @FormUrlEncoded
    fun logIn(
        @Field("user_mail") userMail: String,
        @Field("user_password") userPassword: String
    ): Call<CRUDResponse>

    @POST("coderapp/user_account.php")
    @FormUrlEncoded
    fun findVCode(@Field("user_mail") userMail: String): Call<UserResponse>

    @GET("coderapp/all_account.php")
    fun allAccount(): Call<UserResponse>

    @POST("coderapp/update_verification.php")
    @FormUrlEncoded
    fun updateVerification(@Field("user_mail") user_mail: String): Call<CRUDResponse>

    @POST("coderapp/delete_user.php")
    @FormUrlEncoded
    fun deleteUser(@Field("user_mail") userMail: String): Call<CRUDResponse>


}