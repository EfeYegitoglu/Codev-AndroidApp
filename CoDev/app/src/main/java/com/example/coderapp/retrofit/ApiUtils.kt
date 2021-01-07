package com.example.coderapp.retrofit

import com.example.coderapp.account.UserDaoInterface
import com.example.coderapp.category.CategoryDaoInterface
import com.example.coderapp.post.AddPostActivity
import com.example.coderapp.post.PostDaoInterface
import com.example.coderapp.profile.ProfileDaoInterface

class ApiUtils {

    companion object {
        val BASE_URL = "http://codevsoftware.tk/"

        fun getCategoryDaoInterface(): CategoryDaoInterface {
            return RetrofitClient.getClient(BASE_URL).create(CategoryDaoInterface::class.java)
        }

        fun addUserDaoInterface(): UserDaoInterface {
            return RetrofitClient.getClient(BASE_URL).create(UserDaoInterface::class.java)
        }

        fun profileDaoInterface(): ProfileDaoInterface {
            return RetrofitClient.getClient(BASE_URL).create(ProfileDaoInterface::class.java)
        }

        fun postDaoInterface(): PostDaoInterface {
            return RetrofitClient.getClient(BASE_URL).create(PostDaoInterface::class.java)
        }

    }
}