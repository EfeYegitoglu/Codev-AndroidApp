package com.example.coderapp.post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coderapp.R
import com.example.coderapp.profile.ProfileResponse
import com.example.coderapp.retrofit.ApiUtils
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentAdapter(private val mContext:Context,private val mList:List<Comment>):
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        var commentProfilePicture:CircleImageView
        var commentProfileName:TextView
        var commentText:TextView

        init {
            commentProfilePicture = view.findViewById(R.id.commentProfilePicture)
            commentProfileName = view.findViewById(R.id.commentProfileName)
            commentText = view.findViewById(R.id.commentText)

        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_comment,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return mList.size
    }

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
       val comment = mList[position]

        holder.commentText.text = comment.commentText

        //Post
        val pdi = ApiUtils.postDaoInterface()
        pdi.getPostUserById(comment.userId).enqueue(object : Callback<ProfileResponse> {
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
                                .into(holder.commentProfilePicture)
                        }
                        holder.commentProfileName.text = list.profile_name
                    }

                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {

            }
        })
    }
}