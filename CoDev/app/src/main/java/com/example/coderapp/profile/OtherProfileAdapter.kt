package com.example.coderapp.profile

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.example.coderapp.R
import com.example.coderapp.chat.MessageActivity
import com.example.coderapp.post.CommentActivity
import com.example.coderapp.post.Post
import com.example.coderapp.post.StarSavedResponse
import com.example.coderapp.retrofit.ApiUtils
import com.example.coderapp.retrofit.CRUDResponse
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtherProfileAdapter(private val mContext: Context,private val mList:List<Post>):
    RecyclerView.Adapter<OtherProfileAdapter.ViewHolder>() {

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        var postPicSlider: ImageSlider
        var postProfileImage: ImageView
        var postProfileName: TextView
        var postTitle: TextView
        var postExplanation: TextView
        var postOptions: LinearLayout
        var btnStar: LinearLayout
        var btnComment: LinearLayout
        var btnSavePost: LinearLayout


        init {
            postPicSlider = view.findViewById(R.id.postPicSlider)
            postTitle = view.findViewById(R.id.post_title)
            postExplanation = view.findViewById(R.id.post_explanation)
            postOptions = view.findViewById(R.id.btn_post_options)
            btnStar = view.findViewById(R.id.btn_star)
            btnComment = view.findViewById(R.id.btn_comment)
            btnSavePost = view.findViewById(R.id.btn_saved)
            postProfileName = view.findViewById(R.id.post_profile_name)
            postProfileImage = view.findViewById(R.id.post_profile_picture)


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_post,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val post = mList[position]

        if (post.post_picture_one.isEmpty() && post.post_picture_one.isEmpty() && post.post_picture_one.isEmpty()) {
            holder.postPicSlider.visibility = View.GONE
        } else {
            val imageList = ArrayList<SlideModel>() // Create image list
            if (post.post_picture_one.isNotEmpty()) {
                imageList.add(SlideModel("http://codevsoftware.tk/coderapp/postpictures/" + post.post_picture_one + ".jpg"))
            }

            if (post.post_picture_two.isNotEmpty()) {
                imageList.add(SlideModel("http://codevsoftware.tk/coderapp/postpictures/" + post.post_picture_two + ".jpg"))
            }

            if (post.post_picture_three.isNotEmpty()) {
                imageList.add(SlideModel("http://codevsoftware.tk/coderapp/postpictures/" + post.post_picture_three + ".jpg"))
            }

            holder.postPicSlider.setImageList(imageList)
        }

        holder.postTitle.text = post.post_title
        holder.postExplanation.text = post.post_explanation

        holder.btnComment.setOnClickListener {
            val intent = Intent(this.mContext, CommentActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("post_id",post.post_id)
            this.mContext.startActivity(intent)
        }

        val sharedPreferences = mContext.getSharedPreferences(
            "account_info",
            Context.MODE_PRIVATE
        )

        val userId = sharedPreferences?.getInt("user_id", 0)

        //Post
        val pdi = ApiUtils.postDaoInterface()
        pdi.getPostUserById(post.user_id).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>?
            ) {
                if (response != null) {
                    val userName = response.body()!!.profiles
                    for (list in userName) {
                        if (list.profile_picture.isNotEmpty()) {
                            Picasso.get()
                                .load("http://codevsoftware.tk/coderapp/profileimages/" + list.profile_picture + ".jpg")
                                .into(holder.postProfileImage)
                        }
                        holder.postProfileName.text = list.profile_name
                    }

                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {

            }
        })

        /*------------------------------------------------  STAR START------------------------------------------------------*/
        //Star Control
        var starControl = 0

        pdi.getStar(post.post_id, userId!!).enqueue(object : Callback<StarSavedResponse> {
            override fun onResponse(
                call: Call<StarSavedResponse>,
                response: Response<StarSavedResponse>?
            ) {
                if (response != null) {
                    val starSuccess = response.body()!!.success
                    if (starSuccess == 1) {
                        starControl = 1
                        holder.btnStar.setBackgroundResource(R.drawable.ic_star)
                    } else if (starSuccess == 2) {
                        starControl = 0
                        holder.btnStar.setBackgroundResource(R.drawable.ic_star_empty)
                    }
                }
            }

            override fun onFailure(call: Call<StarSavedResponse>, t: Throwable) {

            }
        })


        //Star
        holder.btnStar.setOnClickListener {

            if (starControl == 0) {
                pdi.insertStar(post.post_id, userId).enqueue(object : Callback<CRUDResponse> {
                    override fun onResponse(
                        call: Call<CRUDResponse>,
                        response: Response<CRUDResponse>?
                    ) {
                        if (response != null) {
                            val success = response.body()!!.success
                            if (success == 1) {
                                holder.btnStar.setBackgroundResource(R.drawable.ic_star)
                                Toast.makeText(mContext, "Gönderi Beğenildi", Toast.LENGTH_SHORT)
                                    .show()
                                starControl = 1
                            }
                        }
                    }

                    override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {

                    }
                })
            } else if (starControl == 1) {
                pdi.deleteStar(post.post_id, userId).enqueue(object : Callback<CRUDResponse> {
                    override fun onResponse(
                        call: Call<CRUDResponse>,
                        response: Response<CRUDResponse>?
                    ) {
                        if (response != null) {
                            val success = response.body()!!.success
                            if (success == 1) {
                                holder.btnStar.setBackgroundResource(R.drawable.ic_star_empty)
                                starControl = 0
                            }
                        }
                    }

                    override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {

                    }
                })
            }

        }

        /*---------------------------------------------  STAR-FINISH ------------------------------------------------------*/

        /*---------------------------------------------  SAVED START ------------------------------------------------------*/

        //Star Control
        var savedControl = 0

        pdi.getSaved(post.post_id, userId).enqueue(object : Callback<StarSavedResponse> {
            override fun onResponse(
                call: Call<StarSavedResponse>,
                response: Response<StarSavedResponse>?
            ) {
                if (response != null) {
                    val starSuccess = response.body()!!.success
                    if (starSuccess == 1) {
                        savedControl = 1
                        holder.btnSavePost.setBackgroundResource(R.drawable.ic_save)
                    } else if (starSuccess == 2) {
                        savedControl = 0
                        holder.btnSavePost.setBackgroundResource(R.drawable.ic_save_empty)
                    }
                }
            }

            override fun onFailure(call: Call<StarSavedResponse>, t: Throwable) {

            }
        })

        //Star
        holder.btnSavePost.setOnClickListener {

            if (savedControl == 0) {
                pdi.insertSaved(post.post_id, userId).enqueue(object : Callback<CRUDResponse> {
                    override fun onResponse(
                        call: Call<CRUDResponse>,
                        response: Response<CRUDResponse>?
                    ) {
                        if (response != null) {
                            val success = response.body()!!.success
                            if (success == 1) {
                                holder.btnSavePost.setBackgroundResource(R.drawable.ic_save)
                                Toast.makeText(mContext, "Gönderi Kaydedildi", Toast.LENGTH_SHORT)
                                    .show()
                                savedControl = 1
                            }
                        }
                    }

                    override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {

                    }
                })
            } else if (savedControl == 1) {
                pdi.deleteSaved(post.post_id, userId).enqueue(object : Callback<CRUDResponse> {
                    override fun onResponse(
                        call: Call<CRUDResponse>,
                        response: Response<CRUDResponse>?
                    ) {
                        if (response != null) {
                            val success = response.body()!!.success
                            if (success == 1) {
                                holder.btnSavePost.setBackgroundResource(R.drawable.ic_save_empty)
                                savedControl = 0
                            }
                        }
                    }

                    override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {

                    }
                })
            }

        }



    }


}