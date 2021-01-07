@file:Suppress("NAME_SHADOWING")

package com.example.coderapp.profile

import android.annotation.SuppressLint
import android.app.AlertDialog.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.coderapp.R
import com.example.coderapp.account.LogInActivity
import com.example.coderapp.post.PostResponse
import com.example.coderapp.post.ProfilePostAdapter
import com.example.coderapp.retrofit.ApiUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.setting_dialog.*
import maes.tech.intentanim.CustomIntent.customType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var profilePostRecycler: RecyclerView
    private lateinit var profilePostAdapter: ProfilePostAdapter


    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)





        getProfileInfo()
        getPostCount()
        getCommentCount()

        val settingIcon = view.findViewById(R.id.profile_settings) as ImageView

        sharedPreferences = activity!!.getSharedPreferences("account_info", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        settingIcon.setOnClickListener {

            val dialogView = LayoutInflater.from(context).inflate(R.layout.setting_dialog, null)

            val alertDialog =
                androidx.appcompat.app.AlertDialog.Builder(context!!).setView(dialogView).show()

            alertDialog.editProfileText.setOnClickListener {
                startActivity(Intent(context, CreateProfileActivity::class.java))
                customType(context, "right-to-left");
            }

            alertDialog.savedOptions.setOnClickListener {
                startActivity(Intent(context, SavedPostActivity::class.java))
                customType(context, "left-to-right")
            }

            alertDialog.signOutText.setOnClickListener {
                val alertView = Builder(context!!)

                alertView.setTitle("CoDev Software'dan çıkış yapıyorsunuz")
                    .setMessage("Emin misiniz?")

                alertView.setPositiveButton("Çıkış Yap") { dialogInterface, i ->

                    editor.clear()
                    editor.apply()


                    startActivity(Intent(context, LogInActivity::class.java))
                    activity!!.finish()
                    alertDialog.dismiss()
                    customType(context, "fadein-to-fadeout")
                }
                alertView.setNegativeButton("İptal") { dialogInterface, i -> }

                alertView.create().show()
            }
        }


        profilePostRecycler = view.findViewById(R.id.profilePostRecycler)
        profilePostRecycler.setHasFixedSize(true)
        profilePostRecycler.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        getProfilePosts()



        return view
    }


    private fun getProfileInfo() {
        val sharedPreferences = context?.getSharedPreferences(
            "account_info",
            Context.MODE_PRIVATE
        )

        val userId = sharedPreferences?.getInt("user_id", 0)
        val userMail = sharedPreferences?.getString("user_mail", null)

        val pdi = ApiUtils.profileDaoInterface()

        pdi.profileQuery(userId!!).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                val profile = response.body()!!.profiles

                for (p in profile) {
                    if (p.profile_picture.isNotEmpty()) {
                        Picasso.get()
                            .load("http://codevsoftware.tk/coderapp/profileimages/" + p.profile_picture + ".jpg")
                            .into(profile_picture)
                    }
                    profile_name.text = p.profile_name
                    profile_mail.text = userMail
                    profile_info.text = p.profile_info
                    if (p.profile_website != "") {
                        profile_website.text = p.profile_website
                    } else {
                        profile_website.visibility = View.GONE
                    }

                }

            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {

            }
        })
    }

    /*PROFILE POSTS*/
    private fun getProfilePosts() {
        val sharedPreferences = context?.getSharedPreferences(
            "account_info",
            Context.MODE_PRIVATE
        )

        val userId = sharedPreferences?.getInt("user_id", 0)

        val pdi = ApiUtils.profileDaoInterface()
        pdi.getProfilePosts(userId!!).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>?) {
                if (response != null) {
                    val success = response.body()!!.success
                    if (success == 1) {
                        val post = response.body()!!.posts
                        profilePostAdapter = ProfilePostAdapter(activity!!, post!!)
                        profilePostRecycler.adapter = profilePostAdapter
                    }
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {

            }
        })

    }

    private fun getPostCount() {
        val sharedPreferences = context?.getSharedPreferences(
            "account_info",
            Context.MODE_PRIVATE
        )

        val userId = sharedPreferences?.getInt("user_id", 0)

        val pdi = ApiUtils.profileDaoInterface()
        pdi.getPostCount(userId!!).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>?) {
                if (response != null) {
                    val success = response.body()!!.success
                    val count = response.body()!!.message
                    if (success == 1) {

                        profilePostCountText.text = count
                    } else if (success == 2) {
                        profilePostCountText.text = count
                    }
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {

            }
        })
    }

    private fun getCommentCount() {
        val sharedPreferences = context?.getSharedPreferences(
            "account_info",
            Context.MODE_PRIVATE
        )

        val userId = sharedPreferences?.getInt("user_id", 0)

        val pdi = ApiUtils.profileDaoInterface()
        pdi.getCommentCount(userId!!).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>?) {
                if (response != null) {
                    val success = response.body()!!.success
                    val count = response.body()!!.message
                    if (success == 1) {

                        profileCommentCountText.text = count
                    } else if (success == 2) {
                        profileCommentCountText.text = count
                    } else {
                        profileCommentCountText.text = "0"
                    }
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {

            }
        })

    }


}
