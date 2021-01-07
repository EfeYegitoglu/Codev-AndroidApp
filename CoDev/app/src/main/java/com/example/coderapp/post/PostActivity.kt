package com.example.coderapp.post

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.coderapp.R
import com.example.coderapp.profile.ProfileResponse
import com.example.coderapp.retrofit.ApiUtils
import com.google.android.material.slider.Slider
import kotlinx.android.synthetic.main.activity_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PostActivity : AppCompatActivity(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var postRecyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val categoryName = intent.getStringExtra("categoryName")


        toolbarPost.title = categoryName?.toUpperCase(Locale.ROOT)
        setSupportActionBar(toolbarPost)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        postRecyclerView = findViewById(R.id.postRecyclerView)
        postRecyclerView.setHasFixedSize(true)
        postRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        getPostsByCategory(categoryName!!)

        refreshPage()

    }

    private fun refreshPage() {
        swipeToRefresh.setOnRefreshListener {
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            swipeToRefresh.isRefreshing = false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val item = menu.findItem(R.id.action_search)
        val searchView = item.actionView as androidx.appcompat.widget.SearchView
        searchView.setOnQueryTextListener(this)
        return true
    }


    override fun onQueryTextSubmit(p0: String?): Boolean {
        Log.e("search", p0.toString())
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        Log.e("search", p0.toString())
        return true
    }

    private fun getPostsByCategory(category: String) {


        val pdi = ApiUtils.postDaoInterface()
        pdi.getPostByCategory(category).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>?) {
                if (response != null){
                    val postList = response.body()!!.posts
                    //Log.e("postList", postList.toString())
                    if (postList != null) {
                        for (pList in postList){

                            if (pList.post_id.toString().isNotEmpty()){
                                postAdapter = PostAdapter(applicationContext, postList)
                                postRecyclerView.adapter = postAdapter
                            }

                        }
                    }else{
                        Toast.makeText(applicationContext, "Bu Kategoride Gönderi Bulunmamaktadır", Toast.LENGTH_SHORT)
                            .show()
                    }

                }



            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Gönderiler görüntülenemiyor", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}


