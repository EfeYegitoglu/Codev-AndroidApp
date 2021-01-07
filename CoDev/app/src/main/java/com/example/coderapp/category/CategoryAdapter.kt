package com.example.coderapp.category

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.coderapp.R
import com.example.coderapp.post.PostActivity
import com.squareup.picasso.Picasso


class CategoryAdapter(private val mContext: Context,private val mList:List<Category>):
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view:View):RecyclerView.ViewHolder(view){
        var category_name:TextView
        var category_picture:ImageView
        var category_post_count:TextView
        var categoryCardView:CardView

        init {
            category_name=view.findViewById(R.id.categoryText)
            category_picture=view.findViewById(R.id.categoryPicture)
            category_post_count=view.findViewById(R.id.categoryPostCountText)
            categoryCardView=view.findViewById(R.id.categoryCardView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
       val view=LayoutInflater.from(mContext).inflate(R.layout.category_layout,parent,false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
       val category=mList[position]

        holder.category_name.text=category.category_name

        val url="http://codevsoftware.tk/coderapp/categorypictures/${category.category_picture}"
        Picasso.get().load(url).into(holder.category_picture)

        holder.category_post_count.text=category.category_post_count.toString()+" post"

        holder.categoryCardView.setOnClickListener {
            val intent = Intent(mContext,PostActivity::class.java)
            intent.putExtra("categoryName",category.category_name)
            mContext.startActivity(intent)
        }
    }
}