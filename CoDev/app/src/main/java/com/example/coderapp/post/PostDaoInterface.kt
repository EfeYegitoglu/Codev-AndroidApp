package com.example.coderapp.post

import com.example.coderapp.profile.ProfileResponse
import com.example.coderapp.retrofit.CRUDResponse
import com.google.gson.annotations.Expose
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PostDaoInterface {

    @POST("coderapp/upload_post.php")
    @FormUrlEncoded
    fun uploadPost(
        @Field("user_id") user_id: Int,
        @Field("post_title") post_title: String,
        @Field("post_explanation") post_explanation: String,
        @Field("post_category") post_category: String,
        @Field("picture_one") picture_one: String,
        @Field("picture_two") picture_two: String,
        @Field("picture_three") picture_three: String
    ): Call<CRUDResponse>

    @POST("coderapp/get_post_by_category.php")
    @FormUrlEncoded
    fun getPostByCategory(
        @Field("post_category") postCategory: String
    ): Call<PostResponse>

    @POST("coderapp/post_user_by_id.php")
    @FormUrlEncoded
    fun getPostUserById(
        @Field("user") user: Int
    ): Call<ProfileResponse>

    @POST("coderapp/post_star_yes.php")
    @FormUrlEncoded
    fun insertStar(
        @Field("post_id") postId: Int,
        @Field("user_id") userId: Int
    ): Call<CRUDResponse>

    @POST("coderapp/get_stars.php")
    @FormUrlEncoded
    fun getStar(
        @Field("post_id") postId: Int,
        @Field("user_id") userId: Int
    ): Call<StarSavedResponse>

    @POST("coderapp/post_star_no.php")
    @FormUrlEncoded
    fun deleteStar(
        @Field("post_id") postId: Int,
        @Field("user_id") userId: Int
    ): Call<CRUDResponse>

    @POST("coderapp/post_saved_yes.php")
    @FormUrlEncoded
    fun insertSaved(
        @Field("post_id") postId: Int,
        @Field("user_id") userId: Int
    ): Call<CRUDResponse>

    @POST("coderapp/get_saved.php")
    @FormUrlEncoded
    fun getSaved(
        @Field("post_id") postId: Int,
        @Field("user_id") userId: Int
    ): Call<StarSavedResponse>

    @POST("coderapp/post_saved_no.php")
    @FormUrlEncoded
    fun deleteSaved(
        @Field("post_id") postId: Int,
        @Field("user_id") userId: Int
    ): Call<CRUDResponse>

    @POST("coderapp/insert_comment.php")
    @FormUrlEncoded
    fun insertComment(
        @Field("post_id") postId: Int,
        @Field("comment_text") commentText: String,
        @Field("user_id") userId: Int
    ): Call<CRUDResponse>

    @POST("coderapp/get_comments.php")
    @FormUrlEncoded
    fun getComments(
        @Field("post_id") postId: Int
    ): Call<CommentResponse>

}