package com.example.coderapp.profile

import com.example.coderapp.post.CommentResponse
import com.example.coderapp.post.Post
import com.example.coderapp.post.PostResponse
import com.example.coderapp.retrofit.CRUDResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ProfileDaoInterface {

    @POST("coderapp/insert_profile.php")
    @FormUrlEncoded
    fun insertProfile(
        @Field("profile_picture") profile_picture: String,
        @Field("profile_name") profile_name: String,
        @Field("profile_info") profile_info: String,
        @Field("profile_website") profile_website: String,
        @Field("user") user: Int
    ): Call<CRUDResponse>

    @POST("coderapp/profile_by_userId.php")
    @FormUrlEncoded
    fun profileQuery(@Field("user") userId: Int): Call<ProfileResponse>

    @POST("coderapp/get_profile_posts.php")
    @FormUrlEncoded
    fun getProfilePosts(
        @Field("user_id") userId: Int
    ): Call<PostResponse>

    @POST("coderapp/get_saved_posts_id.php")
    @FormUrlEncoded
    fun getSavedPostsId(
        @Field("user_id") userId: Int
    ): Call<SavedResponse>

    @POST("coderapp/get_saved_posts_by_post_id.php")
    @FormUrlEncoded
    fun getSavedPostsByPostId(
        @Field("post_id") postId:Int
    ):Call<PostResponse>

    @POST("coderapp/get_post_count.php")
    @FormUrlEncoded
    fun getPostCount(
        @Field("user_id")userId:Int
    ):Call<PostResponse>

    @POST("coderapp/get_comment_count.php")
    @FormUrlEncoded
    fun getCommentCount(
        @Field("user_id")userId:Int
    ):Call<PostResponse>
}