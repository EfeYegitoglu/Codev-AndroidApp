package com.example.coderapp.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coderapp.R
import kotlinx.android.synthetic.main.layout_message_from.view.*
import kotlin.properties.Delegates

class MessageAdapter(
    private val mContext: Context,
    private val userIdList:ArrayList<String>,
    private val messageList:ArrayList<String>,
    private val userId: Int,
    private var state: Boolean = false,
    private val viewTo: Int = 1,
    private val viewFrom: Int = 2
) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var message: TextView?=null



        init {

            if (state){
                message = view.findViewById(R.id.textMessageTo)
            }else{
                message = view.findViewById(R.id.textMessageFrom)
            }






        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View
        return if (viewType == viewTo){
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_message_to,parent,false)
            ViewHolder(view)
        }else {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_message_from,parent,false)
            ViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return userIdList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.message?.text = messageList[position]


    }

    override fun getItemViewType(position: Int): Int {
        return if (userIdList[position] == userId.toString()) {
            state = true
            viewTo
        } else {
            state = false
            viewFrom
        }
    }
}