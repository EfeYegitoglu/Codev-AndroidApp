package com.example.coderapp.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coderapp.R
import com.example.coderapp.post.PostResponse
import com.example.coderapp.retrofit.ApiUtils
import com.google.android.gms.common.api.Api
import kotlinx.android.synthetic.main.activity_saved_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedPostActivity : AppCompatActivity() {
    private lateinit var savedPostRecycler: RecyclerView
    private lateinit var savedPostAdapter: SavedPostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_post)

        val sharedPreferences =
            getSharedPreferences("account_info", Context.MODE_PRIVATE)
        val userId = sharedPreferences?.getInt("user_id", 0)

        Log.i("ui", userId.toString())

        savedPostsToolbar.title = "Kaydedilen Gönderiler"
        setSupportActionBar(savedPostsToolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        savedPostRecycler = findViewById(R.id.savedPostsRecycler)
        savedPostRecycler.setHasFixedSize(true)
        savedPostRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        getSavedPosts(userId!!)

        refreshPage()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun refreshPage() {
        savedPostsSwipe.setOnRefreshListener {
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            savedPostsSwipe.isRefreshing = false
        }
    }

    private fun getSavedPosts(userId: Int) {

        val pdi = ApiUtils.profileDaoInterface()
        pdi.getSavedPostsId(userId).enqueue(object : Callback<SavedResponse> {
            override fun onResponse(call: Call<SavedResponse>, response: Response<SavedResponse>?) {
                if (response != null) {
                    val postId = response.body()!!.saved
                    if (postId != null) {
                        for (id in postId) {
                            Log.i("pi", id.toString())
                            pdi.getSavedPostsByPostId(id.post_id)
                                .enqueue(object : Callback<PostResponse> {
                                    override fun onResponse(
                                        call: Call<PostResponse>,
                                        response: Response<PostResponse>?
                                    ) {
                                        if (response != null) {
                                            val success = response.body()!!.success
                                            if (success == 1) {
                                                val post = response.body()!!.posts

                                                savedPostAdapter =
                                                    post?.let {
                                                        SavedPostsAdapter(applicationContext,
                                                            it
                                                        )
                                                    }!!

                                                savedPostRecycler.adapter = savedPostAdapter


                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<PostResponse>, t: Throwable) {

                                    }
                                })
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SavedResponse>, t: Throwable) {

            }
        })

    }
}