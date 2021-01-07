package com.example.coderapp.post

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coderapp.R
import com.example.coderapp.retrofit.ApiUtils
import com.example.coderapp.retrofit.CRUDResponse
import kotlinx.android.synthetic.main.activity_comment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentActivity : AppCompatActivity() {

    private lateinit var commentRecycler: RecyclerView
    private lateinit var commentAdapter: CommentAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        commentToolbar.title = "Yorumlar"
        setSupportActionBar(commentToolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        val sharedPreferences = getSharedPreferences(
            "account_info",
            Context.MODE_PRIVATE
        )
        val userId = sharedPreferences?.getInt("user_id", 0)
        Log.i("postUserId", userId.toString())
        val postId = intent.getIntExtra("post_id", 0)
        Log.i("postComment", postId.toString())


        btnSendComment.setOnClickListener {


            insertComment(postId, userId!!.toInt())
        }

        refreshPage()

        commentRecycler = findViewById(R.id.commentRecycler)
        commentRecycler.setHasFixedSize(true)
        commentRecycler.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        getCommentByPostId(postId)


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun refreshPage() {
        swipeToRefreshComment.setOnRefreshListener {
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            swipeToRefreshComment.isRefreshing = false
        }
    }

    private fun insertComment(postId: Int, userId: Int) {

        val comment = editTextComment.text.toString()

        if (comment.isNotEmpty()) {
            val pdi = ApiUtils.postDaoInterface()
            pdi.insertComment(postId, comment, userId).enqueue(object : Callback<CRUDResponse> {
                override fun onResponse(
                    call: Call<CRUDResponse>,
                    response: Response<CRUDResponse>?
                ) {
                    if (response != null) {
                        val success = response.body()!!.success
                        val message = response.body()!!.message
                        if (success == 1) {
                            editTextComment.text.clear()
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                            getCommentByPostId(postId)
                        } else {
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {

                }
            })
        } else {
            Toast.makeText(applicationContext, "Lütfen Cevap Giriniz", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getCommentByPostId(postId: Int) {
        val pdi = ApiUtils.postDaoInterface()
        pdi.getComments(postId).enqueue(object : Callback<CommentResponse> {
            override fun onResponse(
                call: Call<CommentResponse>,
                response: Response<CommentResponse>?
            ) {
                if (response != null){
                    val commentSuccess = response.body()!!.success
                    val comment = response.body()!!.comments
                    if (commentSuccess == 1){

                        commentAdapter = comment?.let { CommentAdapter(applicationContext, it) }!!

                        commentRecycler.adapter = commentAdapter


                    }else{

                        Toast.makeText(applicationContext, "Bu gönderi için cevap bulunmamaktadır", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {

            }
        })
    }
}