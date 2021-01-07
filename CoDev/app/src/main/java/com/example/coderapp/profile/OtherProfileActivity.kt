package com.example.coderapp.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coderapp.R
import com.example.coderapp.chat.MessageActivity
import com.example.coderapp.post.PostResponse
import com.example.coderapp.retrofit.ApiUtils
import com.hzn.lib.EasyTransition
import com.r0adkll.slidr.Slidr
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_other_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtherProfileActivity : AppCompatActivity() {

    private lateinit var otherProfileAdapter: OtherProfileAdapter
    private lateinit var otherProfileRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_profile)
        Slidr.attach(this)

        otherProfileRecyclerView = findViewById(R.id.otherProfilePostRecycler)
        otherProfileRecyclerView.setHasFixedSize(true)
        otherProfileRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        val otherProfileId =intent.getIntExtra("other_user_id",0)

        getProfileById(otherProfileId)
        getOtherProfilePosts(otherProfileId)
        getOtherProfilePostCount(otherProfileId)

        buttonMessage.setOnClickListener {
            val intent = Intent(this,MessageActivity::class.java)
            intent.putExtra("other_user_id_for_message",otherProfileId)
            startActivity(intent)
        }



    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun getOtherProfilePosts(userId:Int){


        val pdi = ApiUtils.profileDaoInterface()
        pdi.getProfilePosts(userId).enqueue(object : Callback<PostResponse>{
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>?) {
                if (response != null){
                    val success = response.body()!!.success
                    if (success == 1){
                        val post = response.body()!!.posts
                        otherProfileAdapter =
                            post?.let { OtherProfileAdapter(this@OtherProfileActivity, it) }!!
                        otherProfileRecyclerView.adapter = otherProfileAdapter
                    }

                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {

            }
        })


    }

    private fun getProfileById(userId:Int){
        val pdi = ApiUtils.profileDaoInterface()
        pdi.profileQuery(userId).enqueue(object :Callback<ProfileResponse>{
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>?
            ) {
                if (response != null){
                    val success = response.body()!!.success
                    if (success ==1){
                        val profile = response.body()!!.profiles

                        for (p in profile){
                            other_profile_name.text = p.profile_name
                            other_profile_info.text = p.profile_info
                            if (p.profile_website.isNotEmpty()){
                                other_profile_website.text = p.profile_website
                            }else{
                                other_profile_website.visibility =View.GONE
                            }
                            if (p.profile_picture.isNotEmpty()){
                                Picasso.get()
                                    .load("http://codevsoftware.tk/coderapp/profileimages/" + p.profile_picture + ".jpg")
                                    .into(other_profile_picture)
                            }

                        }
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {

            }
        })
    }

    private fun getOtherProfilePostCount(userId:Int){

        val pdi = ApiUtils.profileDaoInterface()
        pdi.getPostCount(userId).enqueue(object : Callback<PostResponse>{
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>?) {
                if (response != null){
                    val success  = response.body()!!.success
                    if (success == 1){
                        val count = response.body()!!.message

                        otherProfilePostCountText.text = count
                    }
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {

            }
        })
        

    }

}